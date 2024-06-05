package ch.graueenergie.energieclash.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppPropertiesLoader {
    private static final Logger LOGGER = LogManager.getLogger(AppPropertiesLoader.class);
    private static String appPropertiesPath = "/home/pi/deploy/app.properties";
    public static Properties getAppProperties() {
        Properties appProps = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(appPropertiesPath)) {
            appProps.load(fileInputStream);
            LOGGER.info(String.format("Properties file was successfully loaded from path: \"%s\"", appPropertiesPath));
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return appProps;
    }

    public void setAppPropertiesPath(String str) {
        appPropertiesPath = str;
    }
}
