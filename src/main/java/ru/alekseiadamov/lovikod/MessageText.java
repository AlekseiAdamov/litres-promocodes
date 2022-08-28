package ru.alekseiadamov.lovikod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class MessageText {
    private static final Logger logger = LoggerFactory.getLogger(MessageText.class);

    private MessageText() {
        throw new IllegalStateException("Utility class");
    }

    static String create(Collection<PromoCodeInfo> promoCodes) {
        InputStream is = StartPoint.class.getClassLoader().getResourceAsStream("template.html");
        if (is == null) {
            throw new IllegalStateException("Failed to load mail message template");
        }
        String template = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            template = br.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            logger.error("Failed to fill links storage", e);
        }
        final StringBuilder sb = new StringBuilder();
        promoCodes.forEach(promoCode -> sb.append(promoCode.getEmailText()).append("\n"));
        return String.format(template, sb);
    }
}
