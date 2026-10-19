package com.mobile.core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Wraps a single element locator and exposes the interactions the suite needs. */
public class BaseControl {

    protected final By locator;

    /** Creates a control for the given locator.
     * @param locator element locator
     */
    public BaseControl(By locator) {
        this.locator = locator;
    }

    /** Returns the visible element, waiting for it to appear.
     * @return the resolved element
     */
    protected WebElement resolve() {
        return Waits.get().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Clicks the element once it is clickable. */
    public void click() {
        Waits.untilClickable(locator);
        resolve().click();
    }

    /** Tells whether the element is present and visible.
     * @return {@code true} when the element is displayed
     */
    public boolean isDisplayed() {
        return resolve().isDisplayed();
    }

    /** Returns the visible text of the element.
     * @return the element text
     */
    public String getText() {
        return resolve().getText();
    }
}