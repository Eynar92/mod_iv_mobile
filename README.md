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

## Layers

- **Core** — configuration, Appium driver lifecycle, and reusable control wrappers.
- **Business** — screen objects, element locators, and user flows.
- **Test** — the Cucumber feature, step definitions, and hooks.

## Configuration

Device and application settings are read from `src/main/resources/config.properties`.

Repository content and Java documentation are in English. Each class has a concise one-line
Javadoc description, and the framework code is documented in the style of
*Appium in Java* by Cem Kanner: encapsulated driver access, explicit waits instead of
fixed pauses, and screen objects that hold the business logic.