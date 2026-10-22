# Verification - 22 October 2026

## Build
`gradlew.bat assembleDebug assembleDebugAndroidTest --offline --console=plain` - BUILD SUCCESSFUL. Gradle 9.3.0, AGP 9.0.1, compile SDK 36, bundled OpenJDK 21.0.10.

## Engine checks
StopwatchEngineCheck: PASS: 231 checks. Controlled clock values test initial/paused/running time, pause/resume accumulation, duplicate start/pause guards, lap order and limit, reset, serialization, minute/hour boundaries (including 100 hours), hundredths formatting and 1000 repeated start/pause sequences.

## Android checks
Device: emulator-5556 / Medium Phone (2), Android 13 / API 33.
Custom instrumentation uses real Activity Buttons and asserts displayed time, button states and lap rows. Tests include a real Home Intent followed by approximately 10 seconds away and return, paused/running Activity recreation, lap persistence, reset clearing and 100 repeated button sequences.

The exact final runner result and measured comparison intervals are appended below. UI hundredths can lag due to refresh and automation overhead. System.nanoTime is a separate monotonic comparison in the same emulator; no external physical stopwatch comparison has been performed.

## Visual review
Actual portrait emulator captures are in screenshots/. Running/laps screen reviewed for readable time, clear buttons and lap rows with no overlap in the captured view.

## Remaining manual checks
Physical device, true orientation changes, small/landscape scrolling, accessibility, reboot/process-death behavior and comparison with an external independent stopwatch. Saved state reconstruction was tested through Activity.recreate; this is not proof of every process-death scenario.

## Tool warnings
Compatibility WindowInsets/Bundle APIs and android.useAndroidX=false produce non-blocking deprecation warnings with the documented toolchain. No foreground service, internet permission or notification permission is declared.

No GitHub upload, LinkedIn post, video submission or official internship submission has been performed.

## Final device run
PASS: 27 Activity UI checks, including 10-second Home/return, recreation and 100 repeated tap sequences. Pause comparison: UI=1390 ms; nanoTime interval=1431 ms. Background: UI advance=11220 ms; nanoTime=11219 ms.

