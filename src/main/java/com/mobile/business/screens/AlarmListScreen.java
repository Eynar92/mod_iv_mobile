package com.mobile.business.screens;

import com.mobile.core.controls.BaseControl;
import com.mobile.core.controls.Button;
import com.mobile.core.controls.Waits;
import com.mobile.core.driver.AppiumDriverManager;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Screen that lists the configured alarms. */
public class AlarmListScreen extends BaseScreen {

    private static final List<String> OVERLAY_IDS = List.of(
            "material_timepicker_view",
            "material_textinput_timepicker",
            "design_bottom_sheet");

    private static final int MAX_DISMISS_ATTEMPTS = 3;

    private final Button alarmsTab = new Button(id("tab_menu_alarm"));
    private final Button addAlarmButton = new Button(id("fab"));

    /** Selects the Alarms tab and waits for the list to be ready.
     * @return this screen
     */
    public AlarmListScreen open() {
        waitUntilVisible(id("navigation_bar"));
        alarmsTab.click();
        waitUntilVisible(id("alarm_recycler_view"));
        return this;
    }

    /**
     * Closes dialogs and sheets left open by an earlier run, so the scenario always starts
     * from the alarm list. A session can be created while the app still shows a picker.
     * @return this screen
     */
    public AlarmListScreen dismissOverlays() {
        waitUntilAppIsInForeground();
        for (int attempt = 0; attempt < MAX_DISMISS_ATTEMPTS && hasOverlay(); attempt++) {
            AppiumDriverManager.getDriver().navigate().back();
        }
        return this;
    }

    /** Opens the time picker to create a new alarm.
     * @return the time picker screen
     */
    public AlarmTimePickerScreen startNewAlarm() {
        addAlarmButton.click();
        return new AlarmTimePickerScreen();
    }

    /** Tells whether an alarm matching the given text is on screen.
     * @param text time or label of the alarm
     * @return {@code true} when the alarm is listed
     */
    public boolean hasAlarm(String text) {
        return new BaseControl(alarmCard(text)).isPresent();
    }

    /** Tells whether an alarm card contains both the requested time and label.
     * @param time time of the alarm
     * @param label alarm label
     * @return {@code true} when the same alarm card contains both values
     */
    public boolean hasAlarm(String time, String label) {
        return new BaseControl(alarmCard(time, label)).isPresent();
    }

    /** Tells whether an alarm card contains the requested time.
     * @param time time of the alarm
     * @return {@code true} when an alarm at that time is listed
     */
    public boolean hasAlarmAtTime(String time) {
        return hasAlarm(time);
    }

    /** Tells whether an alarm card contains the requested label.
     * @param label alarm label
     * @return {@code true} when an alarm with that label is listed
     */
    public boolean hasAlarmWithLabel(String label) {
        return hasAlarm(label);
    }

    /** Waits for an alarm card containing both the requested time and label.
     * @param time time of the alarm
     * @param label alarm label
     * @return this screen
     */
    public AlarmListScreen waitForAlarm(String time, String label) {
        Waits.untilVisible(alarmCard(time, label));
        return this;
    }

    /** Waits until no alarm card contains both the requested time and label.
     * @param time time of the alarm
     * @param label alarm label
     * @return this screen
     */
    public AlarmListScreen waitForAlarmToDisappear(String time, String label) {
        Waits.untilAbsent(alarmCard(time, label));
        return this;
    }

    /** Waits until no alarm card contains the requested time.
     * @param time time of the alarm
     * @return this screen
     */
    public AlarmListScreen waitForAlarmAtTimeToDisappear(String time) {
        Waits.untilAbsent(alarmCard(time));
        return this;
    }

    /** Opens the editor of the alarm matching the given time.
     * @param time alarm time
     * @return the alarm editor screen
     */
    public AlarmEditorScreen openAlarmAtTime(String time) {
        Waits.untilVisible(alarmCard(time));
        new Button(alarmCard(time)).click();
        return new AlarmEditorScreen();
    }

    /** Opens the editor of the alarm matching both time and label.
     * @param time alarm time
     * @param label alarm label
     * @return the alarm editor screen
     */
    public AlarmEditorScreen openAlarm(String time, String label) {
        Waits.untilVisible(alarmCard(time, label));
        new Button(alarmCard(time, label)).click();
        return new AlarmEditorScreen();
    }

    /** Returns an alarm-card locator whose description contains the given text.
     * @param text text to find in the alarm description
     * @return the alarm card locator
     */
    private By alarmCard(String text) {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/alarm_card'"
                + " and contains(@content-desc, '" + text + "')]");
    }

    /** Returns an alarm-card locator whose description contains both values.
     * @param time alarm time
     * @param label alarm label
     * @return the alarm card locator
     */
    private By alarmCard(String time, String label) {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/alarm_card'"
                + " and contains(@content-desc, '" + time + "')"
                + " and contains(@content-desc, '" + label + "')]");
    }

    /** Tells whether a picker dialog or bottom sheet is still visible.
     * @return {@code true} when a known overlay is present
     */
    private boolean hasOverlay() {
        return OVERLAY_IDS.stream().anyMatch(name -> new BaseControl(id(name)).isPresent());
    }

    /**
     * Waits for any node of the application, which works whether the main screen or a
     * dialog is on top. Waiting for the navigation bar would hang behind an open dialog.
     */
    private void waitUntilAppIsInForeground() {
        Waits.get().until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@package='" + packageName() + "']")));
    }
}
