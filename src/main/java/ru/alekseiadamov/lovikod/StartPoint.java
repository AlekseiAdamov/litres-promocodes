package ru.alekseiadamov.lovikod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        final Config config = Config.get();

        final String pageUrl = config.getPageUrl();
        if (pageUrl == null || pageUrl.isEmpty()) {
            logger.error("Page URL not defined");
            return;
        }

        final String csvPath = config.getCsvPath();
        if (csvPath == null || csvPath.isEmpty()) {
            logger.error("CSV file path not defined");
            return;
        }

        Document promoCodesPage;
        try {
            promoCodesPage = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            logger.error("Failed to get the page");
            return;
        }

        final String elementsQuery = config.getQuery();
        if (elementsQuery == null || elementsQuery.isEmpty()) {
            logger.error("Elements query not defined");
            return;
        }

        // Collect rows with dates, links and descriptions
        Elements tableRows = promoCodesPage.select(elementsQuery);
        if (tableRows.isEmpty()) {
            logger.info("No links found");
            return;
        }

        final File csvFile = Paths.get(csvPath).toFile();
        LinksStorage linksStorage = CsvLinksStorage.load(csvFile);
        final Map<String, PromoCodeInfo> promoCodeInfos = linksStorage.get();

        final Set<PromoCodeInfo> newPromoCodes = tableRows.stream()
                .filter(row -> {
                    final String link = row.select("td:eq(1) strong a[href]")
                            .attr("href")
                            .replaceAll("&utm_source.+$", "")
                            .toLowerCase();
                    return !promoCodeInfos.containsKey(link);
                })
                .map(row -> {
                    final String timeLimit = row.select("td:eq(0)").text();
                    final String link = row.select("td:eq(1) a[href]").attr("href").replaceAll("&utm_source.+$", "");
                    final String description = row.select("td:eq(2)").text();
                    return new PromoCodeInfo(timeLimit, link, description);
                })
                .collect(Collectors.toSet());

        // Store new links to the CSV file
        if (newPromoCodes.isEmpty()) {
            logger.info("No new promo codes");
            return;
        }

        final boolean success = linksStorage.storeAll(newPromoCodes);
        if (!success) {
            logger.error("Failed to store new links");
        }

        // Send and email with links
        // Format:
        // - link
        // - promo code if needed to enter manually (add parameter parsing)
        // - description
        // - time limit

        logger.error("Done, links stored in {}", csvFile);
    }
}
