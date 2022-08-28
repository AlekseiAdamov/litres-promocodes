package ru.alekseiadamov.lovikod.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alekseiadamov.lovikod.PromoCodeInfo;

import java.io.*;
import java.util.*;

public class CsvLinksStorage implements LinksStorage {
    private static final Logger logger = LoggerFactory.getLogger(CsvLinksStorage.class);
    private final Map<String, PromoCodeInfo> links = new HashMap<>();
    private final File path;

    private CsvLinksStorage(File path) {
        this.path = path;
        if (!path.exists()) {
            logger.info("CSV file doesn't exist");
            try {
                final boolean success = path.createNewFile();
                if (!success) {
                    throw new IOException("Unable to create a new file");
                }
            } catch (IOException e) {
                logger.error("Failed to create a CSV file");
            }
        } else {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String linkString;
                while ((linkString = br.readLine()) != null) {
                    final String[] promoCodeInfoFields = linkString.split(",");
                    final String timeLimit = promoCodeInfoFields[1].replace("\"", "");
                    final String link = promoCodeInfoFields[2].replace("\"", "");
                    final String description = promoCodeInfoFields[3].replace("\"", "");
                    links.put(link.toLowerCase(), new PromoCodeInfo(timeLimit, link, description));
                }
            } catch (IOException e) {
                logger.error("Failed to fill links storage", e);
            }
        }
    }

    public static LinksStorage load(File path) {
        return new CsvLinksStorage(path);
    }

    @Override
    public boolean storeAll(Collection<PromoCodeInfo> newLinks) {
        final Date storedDate = new Date();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            for (PromoCodeInfo promoCodeInfo : newLinks) {
                writeLine(writer, promoCodeInfo, storedDate);
            }
        } catch (IOException e) {
            logger.error("Failed to store links", e);
            return false;
        }
        return true;
    }

    private void writeLine(BufferedWriter writer, PromoCodeInfo promoCodeInfo, Date storedDate) throws IOException {
        String csvLine = formatLine(promoCodeInfo, storedDate);
        try {
            writer.append(csvLine);
        } catch (IOException e) {
            throw new IOException(String.format("Failed to store promo code info %s", csvLine), e);
        }
        links.put(promoCodeInfo.getLink().toLowerCase(), promoCodeInfo);
    }

    @Override
    public Map<String, PromoCodeInfo> get() {
        return this.links;
    }

    private String formatLine(PromoCodeInfo info, Date date) {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\"%n", date, info.getTimeLimit(), info.getLink(), info.getDescription());
    }
}
