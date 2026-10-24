# OIBSIP — Android Development Projects

**Intern:** Warisha Ijaz  
**Program:** Oasis Infobyte Internship  
**Technology:** Java and XML, Android Studio

This repository contains three Android applications developed for my internship.

## Projects

| Task | Application | Project folder |
|------|-------------|----------------|
| 1 | Unit Converter | [Open project](Android-Task1-UnitConverter/) |
| 3 | Calculator | [Open project](Android-Task3-Calculator/) |
| 5 | Stopwatch | [Open project](Android-Task5-Stopwatch/) |

### Task 1 — Unit Converter

- Convert length, weight and temperature.
- Select source and target units.
- Swap units.
- Validate empty, invalid and physically impossible values.

### Task 3 — Calculator

- Addition, subtraction, multiplication and division.
- Decimal input, clear, backspace and sign toggle.
- Division-by-zero error handling and recovery.
- Chained operations use immediate, left-to-right calculation.
- Preserve the current calculation during screen rotation.

### Task 5 — Stopwatch

- Start, pause, resume and reset.
- Record lap times.
- Continue measuring elapsed time while the app is in the background.
- Restore the active stopwatch during screen rotation.

## Build Requirements

The projects were built using:

- Android Studio
- JDK 21
- Android SDK Platform 36
- Android Gradle Plugin 9.0.1
- Gradle 9.3.0
- Android 8.0 / API 26 or later to run the apps

## Open and Run

1. Download or clone this repository.
2. Open Android Studio and select **Open**.
3. Select one individual project folder from the table above.
4. Allow Gradle sync to finish and install any required SDK components.
5. Select an Android emulator or connected Android phone.
6. Click **Run**.

Open each application as a separate Android Studio project.

To build from a project's folder on Windows:

    .\gradlew.bat assembleDebug

## Documentation and Screenshots

Each project folder contains its own README, implementation notes,
manual testing checklist and available screenshots.

See each project's documentation for detailed features, setup,
testing status and known limitations.

## Verification Status

- Debug builds completed successfully for all three projects.
- Automated logic and Android instrumentation checks passed in the tested environment.
- Manual emulator checks were also performed.
- Testing does not cover every Android device or accessibility configuration.

## Demonstration Videos

Demonstration videos are being prepared. Links will be added when available.
