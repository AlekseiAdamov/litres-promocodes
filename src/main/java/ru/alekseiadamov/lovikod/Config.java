package ru.alekseiadamov.lovikod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties properties;

    private Config() {
        properties = new Properties();
        try (InputStream is = StartPoint.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (is == null) {
                throw new IllegalStateException("Properties file not found");
            }
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load properties file", e);
        }
    }

    public static Config get() {
        return new Config();
    }

    public String getPageUrl() {
        return properties.getProperty("page.url");
    }

    public boolean ready() {
        return this.properties != null;
    }

    public String getCsvPath() {
        return properties.getProperty("csv.path");
    }

    public String getQuery() {
        return properties.getProperty("page.query");
    }
}
