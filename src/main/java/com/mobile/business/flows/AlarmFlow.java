package com.mobile.business.flows;

import com.mobile.business.screens.AlarmListScreen;

/** User flows that combine several screens into one intent. */
public class AlarmFlow {

    /** Creates an alarm at the given time.
     * @param time time to set, in the form {@code 7:30 AM}
     * @return the alarm list screen holding the new alarm
     * @throws IllegalArgumentException when the time does not match {@code h:mm AM|PM}
     */
    public AlarmListScreen createAlarm(String time) {
        String[] dayPeriod = time.trim().split("\\s+");
        String[] hourMinute = dayPeriod[0].split(":");
        if (dayPeriod.length != 2 || hourMinute.length != 2) {
            throw new IllegalArgumentException("Time must look like '7:30 AM' but was '" + time + "'");
        }

        return new AlarmListScreen().open()
                .startNewAlarm()
                .setTime(zeroPadded(hourMinute[0]), zeroPadded(hourMinute[1]), dayPeriod[1])
                .confirm();
    }

    /** Gives an existing alarm a label.
     * @param time time of the alarm, in the form {@code 7:30 AM}
     * @param label alarm name to assign
     * @return the alarm list screen with the labeled alarm
     */
    public AlarmListScreen labelAlarm(String time, String label) {
        return new AlarmListScreen().open().openAlarmAtTime(time)
                .setLabel(label)
                .save();
    }

    /** Deletes the alarm matching both its time and label.
     * @param time time of the alarm
     * @param label alarm name
     * @return the alarm list screen without that alarm
     */
    public AlarmListScreen deleteAlarm(String time, String label) {
        return new AlarmListScreen().open().openAlarm(time, label).delete();
    }

    /** Deletes a known scenario-owned alarm by its collision-checked time.
     * @param time time of the alarm
     * @return the alarm list screen without that alarm
     */
    public AlarmListScreen deleteAlarmAtTime(String time) {
        return new AlarmListScreen().open().openAlarmAtTime(time).delete();
    }

    /** Returns a zero-padded time field value.
     * @param value numeric time component
     * @return the component with at least two digits
     */
    private String zeroPadded(String value) {
        return String.format("%02d", Integer.parseInt(value));
    }
}
