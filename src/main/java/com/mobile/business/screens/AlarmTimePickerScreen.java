package com.mobile.business.screens;

import com.mobile.core.controls.BaseControl;
import com.mobile.core.controls.Button;
import com.mobile.core.controls.TextBox;
import org.openqa.selenium.By;

/** Screen that sets the time of a new alarm. */
public class AlarmTimePickerScreen extends BaseScreen {

    private final Button modeButton = new Button(id("material_timepicker_mode_button"));
    private final Button okButton = new Button(id("material_timepicker_ok_button"));

    /** Returns the locator of the editable hour field.
     * @return the hour field locator
     */
    private By hourField() {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/material_hour_text_input']"
                + "//android.widget.EditText");
    }

    /** Returns the locator of the editable minute field.
     * @return the minute field locator
     */
    private By minuteField() {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/material_minute_text_input']"
                + "//android.widget.EditText");
    }

    /** Types the hour, the minute, and selects the day period.
     * @param hour hour to type, zero padded
     * @param minute minute to type, zero padded
     * @param period {@code AM} or {@code PM}
     * @return this screen
     */
    public AlarmTimePickerScreen setTime(String hour, String minute, String period) {
        switchToTextInput();
        new TextBox(hourField()).type(hour);
        new TextBox(minuteField()).type(minute);
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
        if (!new BaseControl(hourField()).isPresent()) {
            modeButton.click();
        }
        waitUntilVisible(hourField());
    }

    private boolean isAfternoon(String period) {
        return "PM".equalsIgnoreCase(period);
    }
}