package ru.alekseiadamov.lovikod.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

public class MailConfig {
    private final Properties properties;

    private MailConfig () {
        final Config config = Config.getInstance();
        final String mailPropertiesPath = config.getMailPropertiesPath();
        if (mailPropertiesPath == null || mailPropertiesPath.isEmpty()) {
            throw new IllegalArgumentException("No mail properties provided");
        }
        this.properties = new Properties();
        try (final InputStream is = Files.newInputStream(Paths.get(mailPropertiesPath))) {
            this.properties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load mail properties");
        }
    }

    public static MailConfig get() {
        return new MailConfig();
    }

    public Properties getProperties() {
        return this.properties;
    }

    public String getUser() {
        return this.properties.getProperty("mail.user");
    }

    public String getPassword() {
        return this.properties.getProperty("mail.password");
    }

    public String getFromAddress() {
        return this.properties.getProperty("mail.from.address");
    }

    public String getToAddress() {
        return this.properties.getProperty("mail.to.address");
    }

    public String getTitle() {
        return String.format(this.properties.getProperty("mail.message.title"), new Date());
    }
}
