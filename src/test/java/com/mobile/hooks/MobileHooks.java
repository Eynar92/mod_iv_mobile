package com.mobile.hooks;

import com.mobile.business.flows.AlarmFlow;
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

    /** Cleans up the owned alarm before closing the Appium session. */
    @After
    public void cleanUpAndCloseSession() {
        try {
            if (context.isAlarmCreated() && !context.isAlarmDeleted() && AppiumDriverManager.hasDriver()) {
                AlarmListScreen screen = new AlarmListScreen().dismissOverlays().open();
                if (context.isAlarmLabeled() && screen.hasAlarm(context.getTime(), context.getLabel())) {
                    new AlarmFlow().deleteAlarm(context.getTime(), context.getLabel());
                } else if (screen.hasAlarmAtTime(context.getTime())) {
                    new AlarmFlow().deleteAlarmAtTime(context.getTime());
                }
            }
        } finally {
            AppiumDriverManager.quitDriver();
        }
    }
}
