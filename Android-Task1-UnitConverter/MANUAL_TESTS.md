# UI acceptance checklist

Checked items passed via custom Activity instrumentation on Android 13, 22 October 2026 (programmatic control selection/click). Unchecked items still need manual verification. Toast visibility was not independently asserted.

- [ ] Start app without crash; labels and controls readable.
- [x] 100 centimetres -> metres = 1.
- [x] 1 kilogram -> grams = 1000.
- [x] 0 Celsius -> Fahrenheit = 32.
- [x] 0 Celsius -> Kelvin = 273.15.
- [ ] Empty value -> Toast + field error, no stale result.
- [ ] Invalid numeric text (for example a lone minus sign) -> friendly error.
- [x] Negative length/weight -> rejected.
- [x] Negative Celsius above absolute zero -> valid.
- [x] Below absolute zero -> rejected in all three temperature units.
- [x] Zero and same-unit conversion -> correct.
- [x] Change category -> unit lists reset, stale result cleared.
- [x] Swap exchanges source/target units.
- [x] Repeated taps do not crash.
- [ ] Scroll with keyboard visible on a small screen; Convert remains reachable.
- [ ] Rotate and return; selected category/input retained; conversion still works.
- [ ] Background/return leaves app usable.
- [ ] Very small/large finite values display sensibly; overflow rejected.
