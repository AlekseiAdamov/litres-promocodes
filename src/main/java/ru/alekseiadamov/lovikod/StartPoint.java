package ru.alekseiadamov.lovikod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alekseiadamov.lovikod.storage.CsvLinksStorage;
import ru.alekseiadamov.lovikod.storage.LinksStorage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
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

        // Store new links to the CSV file
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
            sendMailMessage(newPromoCodes, mailPropertiesPath);
        }

        logger.info("Done, links stored in {}", csvFile.getAbsolutePath());
    }

    private static String getLink(Element row) {
        return row.select("td:eq(1) strong a[href]")
                .attr("href")
                .replaceAll("&utm_source.+$", "");
    }

    private static void sendMailMessage(Set<PromoCodeInfo> newPromoCodes, String mailPropertiesPath) {
        Properties mailProperties = new Properties();
        try (final InputStream is = Files.newInputStream(Paths.get(mailPropertiesPath))) {
            mailProperties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load mail properties");
        }
        Session session = Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getProperty("mail.user"), mailProperties.getProperty("mail.password"));
            }
        });
        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailProperties.getProperty("mail.from.address")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailProperties.getProperty("mail.to.address")));
            message.setSubject(String.format(mailProperties.getProperty("mail.message.title"), new Date()));

            final String messageText = MessageText.create(newPromoCodes);
            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            logger.info("Message sent");
        } catch (MessagingException e) {
            logger.error("Failed to send mail message", e);
        }
    }
}
