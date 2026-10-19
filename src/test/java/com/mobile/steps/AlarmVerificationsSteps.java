package com.mobile.steps;

import com.mobile.business.screens.AlarmListScreen;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

/** Verification steps that assert what the alarm list shows. */
public class AlarmVerificationsSteps {

    private final AlarmScenarioContext context;

    /** Creates verification steps with scenario-scoped state.
     * @param context state shared across this scenario
     */
    public AlarmVerificationsSteps(AlarmScenarioContext context) {
        this.context = context;
    }

    /** Waits for the labeled alarm to appear and asserts that it is listed.
     * @param label alarm name
     */
    @Then("the alarm list shows an alarm labeled {string}")
    public void theAlarmListShowsAnAlarmLabeled(String label) {
        Assertions.assertEquals(context.getLabel(), label, "The requested label should match the scenario alarm");
        AlarmListScreen screen = new AlarmListScreen().waitForAlarm(context.getTime(), label);
        Assertions.assertTrue(screen.hasAlarm(context.getTime(), label),
                () -> "Expected one alarm card to show '" + context.getTime() + "' and '" + label + "'");
    }

    /** Waits for the labeled alarm to disappear and asserts that it is gone.
     * @param label alarm name
     */
    @Then("the alarm list no longer shows an alarm labeled {string}")
    public void theAlarmListNoLongerShowsAnAlarmLabeled(String label) {
        Assertions.assertEquals(context.getLabel(), label, "The requested label should match the scenario alarm");
        AlarmListScreen screen = new AlarmListScreen()
                .waitForAlarmToDisappear(context.getTime(), label)
                .waitForAlarmAtTimeToDisappear(context.getTime());
        Assertions.assertFalse(screen.hasAlarm(context.getTime(), label),
                () -> "Expected the scenario alarm to disappear at '" + context.getTime() + "' with label '" + label + "'");
        Assertions.assertFalse(screen.hasAlarmAtTime(context.getTime()),
                () -> "Expected no alarm to remain at the scenario time '" + context.getTime() + "'");
        context.setAlarmDeleted(true);
    }
}
