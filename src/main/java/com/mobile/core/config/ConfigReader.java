package com.mobile.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads the Appium and device settings from the classpath configuration. */
public final class ConfigReader {
    private static final String CONFIG_RESOURCE = "config.properties";
    private final Properties properties;

    /** Loads configuration once when the reader is created.
     * @throws IllegalStateException when the resource is missing or unreadable
     */
    public ConfigReader() {
        properties = new Properties();
        try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (stream == null) {
                throw new IllegalStateException("Required classpath resource config.properties was not found");
            }
            properties.load(stream);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read classpath resource config.properties", exception);
        }
    }

    /** Returns the configured Appium server URL.
     * @return the Appium server URL
     */
    public String getAppiumServerUrl() {
        return required("appium.server.url");
    }

    /** Returns the configured device UDID.
     * @return the device UDID
     */
    public String getDeviceUdid() {
        return required("device.udid");
    }

    /** Returns the configured application package.
     * @return the application package
     */
    public String getAppPackage() {
        return required("app.package");
    }

    /** Returns the configured launcher activity.
     * @return the launcher activity
     */
    public String getAppActivity() {
        return required("app.activity");
    }

    /** Returns a required nonblank configuration property.
     * @param key property name
     * @return configured property value
     * @throws IllegalStateException when the property is missing or blank
     */
    private String required(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration property: " + key);
        }
        return value;
    }
}
