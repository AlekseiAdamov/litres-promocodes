package ru.alekseiadamov.lovikod;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    private static Config config;

    @BeforeAll
    static void setUp() {
        config = Config.get();
    }

    @Test
    void get() {
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
        assertEquals("tr:has(strong a[href~=^https://www.litres.ru/(?!kollekcii-knig).*from=342676429.*$])", config.getQuery());
    }
}
