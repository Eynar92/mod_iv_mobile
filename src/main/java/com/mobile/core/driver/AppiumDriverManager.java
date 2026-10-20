package com.mobile.core.driver;

import com.mobile.core.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import java.net.MalformedURLException;
import java.net.URI;
import org.openqa.selenium.MutableCapabilities;

/** Owns the single Appium driver used for the whole scenario. */
public final class AppiumDriverManager {

    private static final ConfigReader CONFIG = new ConfigReader();
    private static AndroidDriver driver;

    /** Prevents instances of the driver manager. */
    private AppiumDriverManager() {
    }

    /** Returns the shared driver, creating the session on first use.
     * @return the shared Android driver
     * @throws IllegalStateException when the session cannot be created
     */
    public static AndroidDriver getDriver() {
        if (driver == null) {
            driver = createSession();
        }
        return driver;
    }

    /** Quits the session if one is running. Safe to call more than once. */
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }

    /** Tells whether a driver session is currently held.
     * @return {@code true} when a session is active
     */
    public static boolean hasDriver() {
        return driver != null;
    }

    /**
     * Restarts the application under test. A session created after a failed run can resume
     * with a dialog still open, so the process is replaced to start from a known state.
     */
    public static void restartApp() {
        String appPackage = CONFIG.getAppPackage();
        getDriver().terminateApp(appPackage);
        getDriver().activateApp(appPackage);
    }

    /** Creates a new Android Appium session.
     * @return the connected Android driver
     */
    private static AndroidDriver createSession() {
        String udid = CONFIG.getDeviceUdid();
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        capabilities.setCapability("appium:deviceName", udid);
        capabilities.setCapability("appium:udid", udid);
        capabilities.setCapability("appium:appPackage", CONFIG.getAppPackage());
        capabilities.setCapability("appium:appActivity", CONFIG.getAppActivity());
        capabilities.setCapability("appium:noReset", true);
        try {
            return new AndroidDriver(URI.create(CONFIG.getAppiumServerUrl()).toURL(), capabilities);
        } catch (MalformedURLException | IllegalArgumentException exception) {
            throw new IllegalStateException("Invalid Appium server URL in config.properties", exception);
        }
    }
}
