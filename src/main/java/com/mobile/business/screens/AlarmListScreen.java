package com.mobile.business.screens;

import com.mobile.core.controls.BaseControl;
import com.mobile.core.controls.Button;
import com.mobile.core.controls.Waits;
import org.openqa.selenium.By;

/** Screen that lists the configured alarms. */
public class AlarmListScreen extends BaseScreen {

    private final Button alarmsTab = new Button(id("tab_menu_alarm"));
    private final Button addAlarmButton = new Button(id("fab"));

    /** Selects the Alarms tab and waits for the list to be ready.
     * @return this screen
     */
    public AlarmListScreen open() {
        alarmsTab.click();
        waitUntilVisible(id("alarm_recycler_view"));
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
}