package ch.graueenergie.energieclash.controller;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AppPropertiesLoaderTest {
    AppPropertiesLoader appPropertiesLoader = new AppPropertiesLoader();
    Properties properties;

    @Test
    void getAppPropertiesFileFoundTest() {
        appPropertiesLoader.setAppPropertiesPath("src/main/resources/app.properties");

        properties = AppPropertiesLoader.getAppProperties();

        assertNotNull(properties);
        assertFalse(properties.isEmpty());
    }

    @Test
    void getAppPropertiesFileNotFoundTest() {
        appPropertiesLoader.setAppPropertiesPath("");

        properties = AppPropertiesLoader.getAppProperties();

        assertTrue(properties.isEmpty());
    }
}