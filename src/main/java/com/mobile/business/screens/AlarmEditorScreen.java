package com.mobile.business.screens;

import com.mobile.core.controls.Button;
import com.mobile.core.controls.TextBox;

/** Screen that edits an existing alarm. */
public class AlarmEditorScreen extends BaseScreen {

    private final Button labelSetting = new Button(id("label_setting"));
    private final TextBox labelField = new TextBox(id("alarm_label_edit"));
    private final Button saveButton = new Button(id("save_button"));
    private final Button deleteButton = new Button(id("delete_button"));

    /** Focuses the alarm name field and replaces its content.
     * @param label new alarm name
     * @return this screen
     */
    public AlarmEditorScreen setLabel(String label) {
        labelSetting.click();
        labelField.type(label);
        return this;
    }

    /** Saves the alarm and returns to the list.
     * @return the alarm list screen
     */
    public AlarmListScreen save() {
        saveButton.click();
        return new AlarmListScreen().open();
    }

    /** Deletes the alarm and returns to the list.
     * @return the alarm list screen
     */
    public AlarmListScreen delete() {
        deleteButton.click();
        return new AlarmListScreen().open();
    }
}