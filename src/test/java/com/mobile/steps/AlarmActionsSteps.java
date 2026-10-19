package com.mobile.steps;

import com.mobile.business.flows.AlarmFlow;
import com.mobile.business.screens.AlarmListScreen;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/** Action steps that drive the application through the alarm flows. */
public class AlarmActionsSteps {

    private final AlarmScenarioContext context;

    /** Creates the action steps with scenario-scoped state.
     * @param context state shared across this scenario
     */
    public AlarmActionsSteps(AlarmScenarioContext context) {
        this.context = context;
    }

    /** Brings the application to the alarm list.
     * Appium already launches the configured package and activity when the session starts.
     */
    @Given("the Google Clock application is open")
    public void theGoogleClockApplicationIsOpen() {
        new AlarmListScreen().open();
    }

    /** Creates an alarm with the given time and label.
     * @param time alarm time, for example {@code 7:30 AM}
     * @param label alarm name
     */
    @When("the user creates an alarm for {string} with the label {string}")
    public void theUserCreatesAnAlarm(String time, String label) {
        AlarmListScreen alarmList = new AlarmListScreen().open();
        if (alarmList.hasAlarmAtTime(time) || alarmList.hasAlarmWithLabel(label)) {
            throw new IllegalStateException("An alarm already exists at the requested time or with the requested label");
        }
        context.setTime(time);
        context.setLabel(label);
        context.setAlarmCreated(false);
        context.setAlarmLabeled(false);
        context.setAlarmDeleted(false);
        AlarmFlow flow = new AlarmFlow();
        flow.createAlarm(time);
        context.setAlarmCreated(true);
        flow.labelAlarm(time, label);
        context.setAlarmLabeled(true);
    }

    /** Deletes the alarm carrying the given label.
     * @param label alarm name
     */
    @When("the user deletes the alarm labeled {string}")
    public void theUserDeletesTheAlarm(String label) {
        if (!label.equals(context.getLabel())) {
            throw new IllegalArgumentException("The requested label does not match the scenario alarm");
        }
        new AlarmFlow().deleteAlarm(context.getTime(), label);
    }
}
