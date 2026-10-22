# Unit Converter - OIBSIP Task 1

Original Java/XML Android app by Warisha Ijaz, with length, weight and temperature conversions. Works offline, with no network permissions or paid services.

## Required features
- Numeric input and validation with Toast feedback.
- Category selector: Length, Weight, Temperature.
- Source and target Spinners, reset on category change.
- Convert button and labelled result.
- Extras: swap units, inline errors, absolute-zero validation and scrollable layout.

## Build and run
Open this directory in Android Studio. Required: JDK 17 or newer (tested with bundled JDK 21), SDK platform 36, Android Gradle Plugin 9.0.1, Gradle 9.3.0. Minimum Android 8/API 26. Set your SDK location through Android Studio; local.properties is local-only.

Windows: `gradlew.bat assembleDebug`. macOS/Linux: `sh gradlew assembleDebug`.
Select an emulator or connected Android device and press Run. Debug APK: app/build/outputs/apk/debug/app-debug.apk.

## Structure
- MainActivity.java: screen events, validation, result display and saved state.
- ConversionEngine.java: pure conversion logic and unit definitions.
- res/layout/activity_main.xml: scrollable Java/XML interface.
- res/values and res/drawable: labels, theme, colors, rounded surfaces.
- tests/ConversionEngineCheck.java: standalone Java math checks.

## Examples
100 cm = 1 m; 1 kg = 1000 g; 0 Celsius = 32 Fahrenheit = 273.15 Kelvin.
Length uses metres as its base; weight uses kilograms. Temperature uses Celsius as an intermediate value. Negative length/weight and temperatures below absolute zero are rejected. Display uses up to 10 fractional digits or scientific notation for very small/large results; calculations use double precision.

## Validation status
80 standalone Java conversion checks passed, including reference examples, invalid values and round trips across all supported units. 30 Activity UI checks also passed on the Android 13 emulator. Detailed build/device status is recorded in VERIFICATION.md. See MANUAL_TESTS.md for UI acceptance checks. Do not mark unchecked scenarios as passed.

## Screenshots
See screenshots/ when available. Screenshots show actual emulator output; no mockups are used as proof of app behavior.

## Known limits
No currency, history, cloud account or live data. English labels; decimal-dot numeric input. Double arithmetic has finite precision. No Play Store publication is claimed. Test additional device sizes and physical hardware before submission.

## Roman Urdu explanation
XML screen ki layout banata hai. MainActivity buttons aur dropdowns ko handle karti hai. ConversionEngine sirf conversion ka hisaab karti hai. Har category ke units alag hain; category badalne par dropdowns reset hotay hain. Ghalat input par Toast aur field error aata hai.
