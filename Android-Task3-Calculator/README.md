# Calculator - OIBSIP Task 3

Java/XML offline Android calculator by Warisha Ijaz. White cards, teal operators and a large readable display match the Unit Converter app. No network permissions, paid services, accounts or scripting engines.

## Required feature checklist
- TextView input/result and expression history.
- 0-9 keys, decimal point, addition, subtraction, multiplication, division.
- Equals, Clear and Backspace.
- Division by zero displays Error with an explanation.
- XML GridLayout; scrollable on short screens and in landscape.
- Repeated inputs handled by explicit calculator state.
- Extra: sign toggle, digit-limit feedback and Activity recreation state preservation.

## Calculation rules
Immediate execution: operations run left-to-right. `5 + 3 x 2 = 16` (not algebraic precedence).

- One decimal per operand. Decimal first inserts `0.`; duplicate dots are ignored.
- Pressing another operator before a right operand replaces the pending operator.
- Equals with a missing right operand does nothing: `5 + =` waits for the right operand.
- Repeated equals does nothing; it does not repeat the last operation.
- After a result, a digit/decimal starts a fresh calculation; an operator continues from the result.
- Backspace edits the active operand. While waiting for a right operand it does nothing; after a completed result or error it clears the calculation.
- C clears entry, pending operation, expression and errors.
- Sign toggles the current operand; while waiting for an operand it starts a negative operand. A zero result is displayed as 0.
- Errors recover with a digit, decimal, sign or Clear. Operators and Equals are ignored while Error is shown.
- At most 16 input digits, with an explanatory message when extra digits are ignored. Results use 16 significant decimal digits (HALF_UP). Decimal arithmetic avoids the usual binary `0.1 + 0.2` artifact.
- Result magnitudes at or above 1E100 produce Error. Very large/small results use scientific notation. This is a deliberate supported-range limit, not a Java crash.

## Build/run
Open this folder in Android Studio. SDK platform 36, Gradle 9.3.0, Android Gradle Plugin 9.0.1. AGP requires JDK 17 or newer; tested with bundled JDK 21.0.10. Minimum Android 8 / API 26. Select your SDK location locally; local.properties is excluded from source ZIP and Git.

Windows: `gradlew.bat assembleDebug`. macOS/Linux: `sh gradlew assembleDebug`. Select a phone/emulator and press Run. APK: app/build/outputs/apk/debug/app-debug.apk.

## Structure
- CalculatorEngine.java: decimal math, active input, pending operator, error and result state. No Android dependencies.
- MainActivity.java: binds button clicks, updates TextViews and saves/restores engine state.
- res/layout/activity_main.xml: XML GridLayout and display.
- res/values, res/drawable: strings, colors, reusable key styles and ripple backgrounds.
- tests/CalculatorEngineCheck.java: standalone engine checks.
- app/src/androidTest: instrumentation using real Activity buttons, plus recreation.

## Testing and screenshots
See VERIFICATION.md for actual results and MANUAL_TESTS.md for remaining checks. Actual emulator screenshots are in screenshots/. No mockups are used as verification evidence.

## Limits
Basic arithmetic only; no scientific functions, memory, history storage or algebraic precedence. Rounding applies to non-terminating divisions. Hardware keyboard shortcuts and locale-specific number entry are not implemented. Additional physical-device, TalkBack and screen-size checks remain manual.
