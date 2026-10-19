package com.mobile.business.screens;

import com.mobile.core.controls.BaseControl;
import com.mobile.core.controls.Button;
import com.mobile.core.controls.TextBox;
import com.mobile.core.controls.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Screen that sets the time of a new alarm. */
public class AlarmTimePickerScreen extends BaseScreen {

    private static final String HOUR_FIELD = "material_hour_text_input";
    private static final String MINUTE_FIELD = "material_minute_text_input";

    private final Button modeButton = new Button(id("material_timepicker_mode_button"));
    private final Button okButton = new Button(id("material_timepicker_ok_button"));

    /** Types the hour, the minute, and selects the day period.
     * @param hour hour to type, zero padded
     * @param minute minute to type, zero padded
     * @param period {@code AM} or {@code PM}
     * @return this screen
     */
    public AlarmTimePickerScreen setTime(String hour, String minute, String period) {
        switchToTextInput();
        typeInto(HOUR_FIELD, hour);
        typeInto(MINUTE_FIELD, minute);
        new Button(isAfternoon(period) ? id("material_clock_period_pm_button")
                : id("material_clock_period_am_button")).click();
        return this;
    }

    /** Confirms the time and creates the alarm.
     * @return the alarm list screen
     */
    public AlarmListScreen confirm() {
        okButton.click();
        return new AlarmListScreen();
    }

    /** Switches the picker to keyboard input, which is easier to drive than the clock face. */
    private void switchToTextInput() {
        waitUntilDialogIsRendered();
        if (!new BaseControl(id(HOUR_FIELD)).isPresent()) {
            modeButton.click();
            waitUntilVisible(id(HOUR_FIELD));
        }
    }

    /**
     * Waits until the dialog shows its content in either mode. The picker remembers the
     * last used mode, so the suite must read the current mode instead of assuming one.
     */
    private void waitUntilDialogIsRendered() {
        Waits.get().until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(id(HOUR_FIELD)),
                ExpectedConditions.visibilityOfElementLocated(id("material_clock_face"))));
    }

    /**
     * Focuses a field and replaces its content. Material only renders the editable
     * element while the field holds the focus, so the tap has to come first.
     */
    private void typeInto(String fieldId, String value) {
        new Button(fieldArea(fieldId)).click();
        new TextBox(fieldEditor(fieldId)).type(value);
    }

    /** Returns the tappable area of a field, which exists whether or not it has focus.
     * @param fieldId resource id of the field container
     * @return the field area locator
     */
    private By fieldArea(String fieldId) {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/" + fieldId + "']//*[@clickable='true']");
    }

    /** Returns the editable element, which Material renders only on the focused field.
     * @param fieldId resource id of the field container
     * @return the editable element locator
     */
    private By fieldEditor(String fieldId) {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/" + fieldId + "']//android.widget.EditText");
    }

    private boolean isAfternoon(String period) {
        return "PM".equalsIgnoreCase(period);
    }
}