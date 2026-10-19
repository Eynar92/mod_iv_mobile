package com.mobile.core.controls;

import com.mobile.core.driver.AppiumDriverManager;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Builds explicit waits so the suite never relies on fixed pauses. */
public final class Waits {

    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    /** Prevents instances of this utility class. */
    private Waits() {
    }

    /** Returns a wait bound to the shared driver.
     * @return a wait using the default timeout
     */
    public static WebDriverWait get() {
        return new WebDriverWait(AppiumDriverManager.getDriver(), TIMEOUT);
    }

    /** Waits until an element matching the locator is visible.
     * @param locator element locator
     */
    public static void untilVisible(By locator) {
        get().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Waits until an element matching the locator is clickable.
     * @param locator element locator
     */
    public static void untilClickable(By locator) {
        get().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /** Waits until no visible element matches the locator.
     * @param locator element locator
     */
    public static void untilAbsent(By locator) {
        get().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
