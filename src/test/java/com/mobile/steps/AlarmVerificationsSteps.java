package com.mobile.steps;

import com.mobile.business.screens.AlarmListScreen;
import com.mobile.core.controls.Waits;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

/** Verification steps that assert what the alarm list shows. */
public class AlarmVerificationsSteps {

    /** Waits for the labeled alarm to appear and asserts that it is listed.
     * @param label alarm name
     */
    @Then("the alarm list shows an alarm labeled {string}")
    public void theAlarmListShowsAnAlarmLabeled(String label) {
        AlarmListScreen screen = new AlarmListScreen();
        Waits.untilVisible(screen.alarmCard(label));
        Assertions.assertTrue(screen.hasAlarm(label),
                () -> "Expected the alarm list to show an alarm labeled '" + label + "'");
    }

    /** Waits for the labeled alarm to disappear and asserts that it is gone.
     * @param label alarm name
     */
    @Then("the alarm list no longer shows an alarm labeled {string}")
    public void theAlarmListNoLongerShowsAnAlarmLabeled(String label) {
        AlarmListScreen screen = new AlarmListScreen();
        Waits.untilAbsent(screen.alarmCard(label));
        Assertions.assertFalse(screen.hasAlarm(label),
                () -> "Expected the alarm list not to show an alarm labeled '" + label + "'");
    }
}