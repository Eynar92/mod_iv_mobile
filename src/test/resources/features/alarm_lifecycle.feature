Feature: Google Clock alarm management

  Scenario: Create an alarm, verify it is listed, and delete it as cleanup
    Given the Google Clock application is open
    When the user creates an alarm for "7:30 AM" with the label "Diplomado IV"
    Then the alarm list shows an alarm labeled "Diplomado IV"
    When the user deletes the alarm labeled "Diplomado IV"
    Then the alarm list no longer shows an alarm labeled "Diplomado IV"