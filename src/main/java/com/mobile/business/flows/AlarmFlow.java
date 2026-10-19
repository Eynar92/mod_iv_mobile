package com.mobile.business.flows;

import com.mobile.business.screens.AlarmListScreen;

/** User flows that combine several screens into one intent. */
public class AlarmFlow {

    /** Creates an alarm at the given time and gives it a label.
     * @param time time to set, in the form {@code 7:30 AM}
     * @param label alarm name to assign
     * @return the alarm list screen holding the new alarm
     * @throws IllegalArgumentException when the time does not match {@code h:mm AM|PM}
     */
    public AlarmListScreen createAlarm(String time, String label) {
        String[] dayPeriod = time.trim().split("\\s+");
        String[] hourMinute = dayPeriod[0].split(":");
        if (dayPeriod.length != 2 || hourMinute.length != 2) {
            throw new IllegalArgumentException("Time must look like '7:30 AM' but was '" + time + "'");
        }

        return new AlarmListScreen().open()
                .startNewAlarm()
                .setTime(zeroPadded(hourMinute[0]), zeroPadded(hourMinute[1]), dayPeriod[1])
                .confirm()
                .openAlarm(time)
                .setLabel(label)
                .save();
    }

    /** Deletes the alarm carrying the given label.
     * @param label alarm name of the alarm to delete
     * @return the alarm list screen without that alarm
     */
    public AlarmListScreen deleteAlarm(String label) {
        return new AlarmListScreen().open().openAlarm(label).delete();
    }

    private String zeroPadded(String value) {
        return String.format("%02d", Integer.parseInt(value));
    }
}