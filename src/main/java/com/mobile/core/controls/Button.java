package com.mobile.core.controls;

import org.openqa.selenium.By;

/** Represents a clickable element such as a button, a tab, or a list row. */
public class Button extends BaseControl {

    /** Creates a clickable control.
     * @param locator element locator
     */
    public Button(By locator) {
        super(locator);
    }
}