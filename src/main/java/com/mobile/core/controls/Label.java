package com.mobile.core.controls;

import org.openqa.selenium.By;

/** Represents a read-only element that displays text. */
public class Label extends BaseControl {

    /** Creates a read-only control.
     * @param locator element locator
     */
    public Label(By locator) {
        super(locator);
    }

    /** Tells whether the visible text contains the given fragment.
     * @param fragment text fragment to look for
     * @return {@code true} when the text contains the fragment
     */
    public boolean contains(String fragment) {
        return getText().contains(fragment);
    }
}