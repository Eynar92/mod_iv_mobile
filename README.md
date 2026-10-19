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
`openAlarm(String)`, so step definitions never build locators themselves.

`com.mobile.business.flows.AlarmFlow` chains screens into the flows the scenario needs:
`createAlarm(String time, String label)` and `deleteAlarm(String label)`.

The time picker is driven in keyboard mode rather than through the clock face. The numbers on
the face expose only an accessibility description and no resource id, whereas switching with
`material_timepicker_mode_button` reveals real `EditText` fields for the hour and the minute.

## Test layer

The Cucumber feature, step definitions, and hooks live in `src/test/java/com/mobile`.

Repository content and Java documentation are in English. Each class has a concise one-line
Javadoc description, and the framework code is documented in the style of
*Appium in Java* by Cem Kanner: encapsulated driver access, explicit waits instead of
fixed pauses, and screen objects that hold the business logic.