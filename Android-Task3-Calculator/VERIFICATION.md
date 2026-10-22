# Actual verification - 22 October 2026

## Build
`gradlew.bat assembleDebug assembleDebugAndroidTest --offline --console=plain` - BUILD SUCCESSFUL.
Gradle 9.3.0, AGP 9.0.1, SDK 36, bundled OpenJDK 21.0.10.

## Pure Java engine checks
CalculatorEngineCheck: PASS: 33 engine checks, including 500 repeated input sequences. Covers reference arithmetic, decimal precision, negative results, sign, operator replacement, missing operand, repeated equals, backspace, clear, chaining, error recovery, digit limit, range overflow and serialized state restoration.

## Device checks
Custom instrumentation on emulator-5556, Medium Phone (2), Android 13 / API 33:
PASS: 25 Activity UI checks, including Activity recreation and 100 rapid input sequences.

Real Activity Buttons were invoked programmatically on the UI thread; displayed TextView results were asserted. Covered all four operations, decimal-first/duplicate decimal, operator replacement, left-to-right chaining, missing operand, repeated equals, backspace, clear, negative input, long input, divide-by-zero and recovery. Activity was recreated while 123 + 4 was pending; pressing equals after recreation produced 127.

Screenshots addition.png, decimal.png, error.png, long-input.png and final.png are actual emulator captures during instrumentation. Addition screen was visually reviewed: all keys and display fit, text is readable, no overlap in this view.

## Still manual
Physical device, actual rotation button/orientation change (Activity recreation was automated), TalkBack, human rapid-tap timing, landscape scrolling and smaller screen sizes. See MANUAL_TESTS.md. Automated button calls are not a claim that every touch target and device has been tested.

## Non-blocking tool warnings
android.useAndroidX=false and compatibility APIs used for older Android versions produce deprecation warnings. Current documented build succeeds. No library/toolchain upgrade was performed solely to remove warnings.

No GitHub upload, LinkedIn post or internship submission has been performed.
