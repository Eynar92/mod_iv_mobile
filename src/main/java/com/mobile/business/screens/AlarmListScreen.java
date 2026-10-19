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

    /** Returns the locator of the alarm card whose description contains the given text.
     * @param text time or label of the alarm
     * @return the alarm card locator
     */
    public By alarmCard(String text) {
        return By.xpath("//*[@resource-id='" + packageName() + ":id/alarm_card'"
                + " and contains(@content-desc, '" + text + "')]");
    }

    /** Tells whether an alarm matching the given text is on screen.
     * @param text time or label of the alarm
     * @return {@code true} when the alarm is listed
     */
    public boolean hasAlarm(String text) {
        return new BaseControl(alarmCard(text)).isPresent();
    }

    /** Opens the editor of the alarm matching the given text.
     * @param text time or label of the alarm
     * @return the alarm editor screen
     */
    public AlarmEditorScreen openAlarm(String text) {
        Waits.untilVisible(alarmCard(text));
        new Button(alarmCard(text)).click();
        return new AlarmEditorScreen();
    }

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