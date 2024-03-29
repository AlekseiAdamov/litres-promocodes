package ru.alekseiadamov.lovikod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alekseiadamov.lovikod.config.Config;
import ru.alekseiadamov.lovikod.message.MailMessage;
import ru.alekseiadamov.lovikod.message.MessageText;
import ru.alekseiadamov.lovikod.storage.CsvLinksStorage;
import ru.alekseiadamov.lovikod.storage.LinksStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StartPoint {
    private static final Logger logger = LoggerFactory.getLogger(StartPoint.class);

    public static void main(String[] args) {
        final Config config = Config.getInstance();

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

        // All promo code data is contained in a table.
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
                    final String link = getLink(row).toLowerCase();
                    return !promoCodeInfos.containsKey(link);
                })
                .map(row -> {
                    final String timeLimit = row.select("td:eq(0)").text();
                    final String link = getLink(row);
                    final String description = row.select("td:eq(2)").text();
                    return new PromoCodeInfo(timeLimit, link, description);
                })
                .collect(Collectors.toSet());

        if (newPromoCodes.isEmpty()) {
            logger.info("No new promo codes");
            return;
        }
        logger.info("{} new promo codes", newPromoCodes.size());

        final boolean success = linksStorage.storeAll(newPromoCodes);
        if (!success) {
            logger.error("Failed to store new links");
        }

        final String mailPropertiesPath = config.getMailPropertiesPath();
        if (mailPropertiesPath != null && !mailPropertiesPath.isEmpty()) {
            final String messageText = MessageText.create(newPromoCodes);
            final MailMessage mailMessage = MailMessage.create(messageText);
            mailMessage.send();
            logger.info("Message sent");
        }

        logger.info("Done, links stored in {}", csvFile.getAbsolutePath());
    }

    private static String getLink(Element row) {
        return row.select("td:eq(1) a.promocode[href]")
                .attr("href")
                .replaceAll("&utm_source.+$", "");
    }
}
