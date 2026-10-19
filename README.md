# Google Clock Mobile Automation

Mobile test automation for the Google Clock Android application, built with Java, Gradle,
Appium, and Cucumber. The project follows a three-layer architecture that keeps Appium
driver management, screen objects, and test code separated.

## Requirements

- JDK 21
- Android SDK with `adb` available on the `PATH`
- Node.js and Appium with the UiAutomator2 driver
- A running Android device or emulator visible to `adb devices`
- The application under test installed on that device

The Gradle Wrapper is included, so no separate Gradle installation is required.

## Build

The Wrapper runs Gradle 8.14.3 and the Java toolchain targets Java 21.

```powershell
.\gradlew.bat classes
```

The `cucumberCli` task runs the Cucumber scenarios and writes an HTML report to
`build/reports/cucumber/report.html`. It requires at least one `.feature` file under
`src/test/resources/features`, which the test layer provides.

## Layers

- **Core** — configuration, Appium driver lifecycle, and reusable control wrappers.
- **Business** — screen objects, element locators, and user flows.
- **Test** — the Cucumber feature, step definitions, and hooks.

## Configuration

Device and application settings are read from `src/main/resources/config.properties` by
`com.mobile.core.config.ConfigReader`.

| Property | Purpose |
| --- | --- |
| `appium.server.url` | Base URL of the local Appium server. |
| `device.udid` | UDID of the device or emulator under test. |
| `app.package` | Android package of the application under test. |
| `app.activity` | Launcher activity of the application under test. |

Retrieve the package and activity of any installed app with:

```powershell
adb shell cmd package resolve-activity --brief com.google.android.deskclock
```

## Core layer

`com.mobile.core.driver.AppiumDriverManager` owns a single Android session and is the only
place that builds capabilities, so no other class calls `new AndroidDriver(...)`. The session
uses `noReset`, which keeps the device data between runs.

`com.mobile.core.controls` wraps element interaction. `BaseControl` exposes `click()`,
`getText()`, and `isDisplayed()`, and the `Button`, `TextBox`, and `Label` subclasses add the
behaviour their role needs. Every lookup goes through `Waits`, a fifteen second
`WebDriverWait`, so the suite uses condition based waits and never `Thread.sleep`.

## Business layer

`com.mobile.business.screens` holds one class per screen. `AlarmListScreen` covers the alarm
list, `AlarmTimePickerScreen` the time picker dialog, and `AlarmEditorScreen` the editor sheet.
Each screen keeps its own locators as private constants and exposes intent methods such as
`openAlarm(String time, String label)`, so step definitions never build locators themselves.

`com.mobile.business.flows.AlarmFlow` chains screens into the flows the scenario needs:
`createAlarm(String time)`, `labelAlarm(String time, String label)`, and delete operations by
time with a label or by the scenario-owned time alone.

The time picker is driven in keyboard mode rather than through the clock face. The numbers on
the face expose only an accessibility description and no resource id, whereas switching with
`material_timepicker_mode_button` reveals real `EditText` fields for the hour and the minute.

Material only renders the editable element while a field holds the focus, so each field is
tapped before it is typed into. The picker also remembers the last mode it was left in, so
`AlarmTimePickerScreen` reads the current mode instead of assuming one.

## Test layer

`src/test/resources/features/alarm_lifecycle.feature` holds a single scenario: open the
application, create an alarm for `7:30 AM` named `Diplomado IV`, verify the same alarm card
shows both its time and label, delete it, and verify the alarm is gone. Before creating anything,
the action checks that no alarm already uses the requested time or label. It records ownership
only after the time is confirmed, so the cleanup hook can remove the created alarm by its known
time even if labeling fails.

`AlarmActionsSteps` holds the `Given` and `When` steps and delegates to `AlarmFlow`.
`AlarmVerificationsSteps` holds the `Then` steps and owns every assertion. `MobileHooks` starts
one Appium session per scenario, removes only an alarm created by that scenario if needed, and
closes the session after cleanup even when cleanup fails.

## Run

Start Appium, make sure the device is visible to `adb devices`, then:

```powershell
.\gradlew.bat cucumberCli
```

The report is written to `build/reports/cucumber/report.html`. Running the task again is safe:
the scenario removes the alarm it creates. If another alarm already uses `7:30 AM` or the label
`Diplomado IV`, the scenario fails before creating or changing any alarm.

## Project structure

```text
mod_iv_mobile/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
└── src/
    ├── main/
    │   ├── java/com/mobile/
    │   │   ├── core/config/ConfigReader.java
    │   │   ├── core/driver/AppiumDriverManager.java
    │   │   ├── core/controls/{BaseControl,Button,TextBox,Label,Waits}.java
    │   │   └── business/
    │   │       ├── screens/{BaseScreen,AlarmListScreen,AlarmEditorScreen,AlarmTimePickerScreen}.java
    │   │       └── flows/AlarmFlow.java
    │   └── resources/config.properties
    └── test/
        ├── java/com/mobile/
        │   ├── hooks/MobileHooks.java
        │   └── steps/{AlarmActionsSteps,AlarmVerificationsSteps,AlarmScenarioContext}.java
        └── resources/features/alarm_lifecycle.feature
```

## Notes

- The session is created with `noReset`, so the device keeps its data between runs. The hook
  restarts the application before each scenario, because a session created after a failed run
  can resume with a dialog still open.
- Google Clock asks for `POST_NOTIFICATIONS` on first launch. On the reference emulator the
  permission is already granted; otherwise run
  `adb shell pm grant com.google.android.deskclock android.permission.POST_NOTIFICATIONS`.
- The scenario assumes the device shows times in the 12 hour English format, because it matches
  the time and label rendered in the same alarm card's content description.

Repository content and Java documentation are in English. Each class has a concise one-line
Javadoc description. The code keeps driver management centralized, uses explicit waits instead
of fixed pauses, and stores locators and screen operations in screen objects. The scenario is the
project's end-to-end validation; there are no separate unit tests for the framework. These changes
were statically reviewed; the mobile scenario was not run.
