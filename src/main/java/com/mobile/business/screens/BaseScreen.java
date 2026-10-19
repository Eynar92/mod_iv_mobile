package com.mobile.business.screens;

import com.mobile.core.controls.Waits;
import org.openqa.selenium.By;

/** Shared waits and identifiers for every screen of the application. */
public abstract class BaseScreen {

    /** Android package of the application under test, used to qualify resource ids.
     * @return the application package
     */
    protected String packageName() {
        return "com.google.android.deskclock";
    }

    /** Builds a resource id locator for this application.
     * @param id the resource id without the package prefix
     * @return the qualified locator
     */
    protected By id(String id) {
        return By.id(packageName() + ":id/" + id);
    }

    /** Waits until an element is visible.
     * @param locator element locator
     */
    protected void waitUntilVisible(By locator) {
        Waits.untilVisible(locator);
    }

    /** Waits until an element is no longer visible.
     * @param locator element locator
     */
    protected void waitUntilAbsent(By locator) {
        Waits.untilAbsent(locator);
    }
}