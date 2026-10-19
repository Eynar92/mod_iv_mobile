package com.mobile.steps;

/** Holds the state shared by the action, verification, and hook classes of one scenario. */
public class AlarmScenarioContext {

    private String time;
    private String label;
    private boolean alarmCreated;
    private boolean alarmLabeled;
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

    /** Tells whether this scenario created its alarm.
     * @return {@code true} once the time was confirmed in the picker
     */
    public boolean isAlarmCreated() {
        return alarmCreated;
    }

    /** Records ownership after the alarm time is confirmed.
     * @param alarmCreated {@code true} after confirmation
     */
    public void setAlarmCreated(boolean alarmCreated) {
        this.alarmCreated = alarmCreated;
    }

    /** Tells whether the scenario saved its alarm label.
     * @return {@code true} after the label is saved
     */
    public boolean isAlarmLabeled() {
        return alarmLabeled;
    }

    /** Records whether the scenario saved its alarm label.
     * @param alarmLabeled {@code true} after the label is saved
     */
    public void setAlarmLabeled(boolean alarmLabeled) {
        this.alarmLabeled = alarmLabeled;
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
