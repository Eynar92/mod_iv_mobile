package com.mobile.steps;

/** Holds the state shared by the action, verification, and hook classes of one scenario. */
public class AlarmScenarioContext {

    private String time;
    private String label;
    private boolean alarmDeleted;

    /** Returns the time the scenario alarm was created with.
     * @return the alarm time
     */
    public String getTime() {
        return time;
    }

    /** Records the time the scenario alarm was created with.
     * @param time alarm time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /** Returns the label of the scenario alarm.
     * @return the alarm label
     */
    public String getLabel() {
        return label;
    }

    /** Records the label of the scenario alarm.
     * @param label alarm label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /** Tells whether the scenario already deleted its own alarm.
     * @return {@code true} when the alarm was deleted by the scenario
     */
    public boolean isAlarmDeleted() {
        return alarmDeleted;
    }

    /** Marks the scenario alarm as deleted.
     * @param alarmDeleted {@code true} once the alarm is gone
     */
    public void setAlarmDeleted(boolean alarmDeleted) {
        this.alarmDeleted = alarmDeleted;
    }
}