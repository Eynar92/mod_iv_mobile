package com.mobile.core.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** Represents an editable text field. */
public class TextBox extends BaseControl {

    /** Creates an editable control.
     * @param locator element locator
     */
    public TextBox(By locator) {
        super(locator);
    }

    /** Replaces the current content with the given text.
     * @param text value to type
     */
    public void type(String text) {
        WebElement element = resolve();
        element.clear();
        element.sendKeys(text);
    }
}