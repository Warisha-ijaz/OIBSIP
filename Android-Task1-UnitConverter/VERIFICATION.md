# Verification - 22 October 2026

- Build: `gradlew.bat assembleDebug --offline --console=plain` - BUILD SUCCESSFUL.
- Toolchain: Gradle 9.3.0, AGP 9.0.1, bundled OpenJDK 21.0.10, compile SDK 36.
- Pure Java tests: ConversionEngineCheck - PASS: 80 conversion checks.
- Android 13 / API 33 Medium_Phone emulator: APK installed successfully and MainActivity launched.
- Actual initial screen captured at screenshots/home.png and visually reviewed; labels, input, controls and result card are readable without overlap in the captured view.
- AndroidRuntime error log showed no fatal exception after launch.
- Custom instrumentation on Android 13: PASS: 30 Activity UI checks. Verified actual reference conversions in all categories, same units, zero, negative temperature, absolute-zero boundaries, empty/non-numeric/negative input field errors, stale-result clearing, category defaults, swap, large finite input and repeated Convert clicks.
- Instrumentation checks invoke real Activity controls on the main thread. They verify inline validation; transient Toast visibility and human touch/keyboard interactions were not separately asserted.
- Rotation, physical-device testing, TalkBack and small-screen keyboard behavior remain manual checks.
- Screenshots length.png, weight.png, temperature.png and final-length.png were captured during instrumentation. Temperature result screenshot visually reviewed with no overlap.
- Non-blocking warnings: legacy compatibility WindowInsets APIs and the android.useAndroidX=false Gradle option are deprecated. Build is successful with the documented current toolchain.

This is a working first implementation, not a claim of final internship submission or full device compatibility verification.
