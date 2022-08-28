package ru.alekseiadamov.lovikod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();
    private static Config instance;

    private Config() {
        try (InputStream is = StartPoint.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (is == null) {
                throw new IllegalStateException("Properties file not found");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load properties file", e);
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getPageUrl() {
        return properties.getProperty("page.url");
    }

    public boolean ready() {
        return properties != null;
    }

    public String getCsvPath() {
        return properties.getProperty("csv.path");
    }

    public String getQuery() {
        return properties.getProperty("page.query");
    }

    public String getMailPropertiesPath() {
        return properties.getProperty("mail.properties.path");
    }
}
