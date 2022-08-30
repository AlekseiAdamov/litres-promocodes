package ru.alekseiadamov.lovikod;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.alekseiadamov.lovikod.config.Config;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    private static Config config;

    @BeforeAll
    static void setUp() {
        config = Config.getInstance();
    }

    @Test
    void getInstance() {
        assertInstanceOf(Config.class, config);
    }

    @Test
    void getPageUrl() {
        assertEquals("https://lovikod.ru/knigi/promokody-litres", config.getPageUrl());
    }

    @Test
    void ready() {
        assertTrue(config.ready());
    }

    @Test
    void getCsvPath() {
        assertEquals("./litres.csv", config.getCsvPath());
    }

    @Test
    void getQuery() {
        assertEquals("tr:has(td:eq(1) strong a[href~=^https://www.litres.ru/(?!kollekcii-knig).*&lfrom=342676429.*$])", config.getQuery());
    }

    @Test
    void getMailPropertiesPath() {
        assertEquals("./mail.properties", config.getMailPropertiesPath());
    }
}
