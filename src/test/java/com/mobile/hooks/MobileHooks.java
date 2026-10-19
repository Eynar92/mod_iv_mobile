package com.mobile.hooks;

import com.mobile.business.screens.AlarmListScreen;
import com.mobile.core.driver.AppiumDriverManager;
import com.mobile.steps.AlarmScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

/** Manages the Appium session and the data the scenario creates. */
public class MobileHooks {

    private final AlarmScenarioContext context;

    /** Creates the hooks with scenario-scoped state.
     * @param context state shared across this scenario
     */
    public MobileHooks(AlarmScenarioContext context) {
        this.context = context;
    }

    /** Starts a single Appium session and restarts the app for a known starting state. */
    @Before
    public void openSession() {
        AppiumDriverManager.quitDriver();
        AppiumDriverManager.getDriver();
        AppiumDriverManager.restartApp();
    }

    /** Removes the scenario alarm when the scenario failed before deleting it. */
    @After(order = 0)
    public void removeScenarioAlarm() {
        if (context.getLabel() == null || context.isAlarmDeleted()) {
            return;
        }
        AlarmListScreen screen = new AlarmListScreen().dismissOverlays().open();
        if (screen.hasAlarm(context.getLabel())) {
            screen.openAlarm(context.getLabel()).delete();
        }
    }

    /** Closes the Appium session. */
    @After(order = 10)
    public void closeSession() {
        AppiumDriverManager.quitDriver();
    }
}