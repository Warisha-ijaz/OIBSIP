# Human acceptance checklist

Automated tests are in VERIFICATION.md. Check these only after observing them on your device.

- [ ] Initial time 00:00:00 and .00; Start active, Pause/Lap inactive.
- [ ] Start; wait around 5 seconds; time advances normally.
- [ ] Start is greyed out while running; Pause and Lap active.
- [ ] Pause; wait around 3 seconds; display remains frozen.
- [ ] Resume; time continues from paused value.
- [ ] Record two laps; cumulative elapsed and split times appear newest first.
- [ ] While running press Home; wait around 10 seconds; return through Recents/app icon. Elapsed includes time away.
- [ ] Rotate while running, return to portrait; timer keeps correct accumulated state.
- [ ] Rotate while paused; paused time and laps preserved.
- [ ] Reset stops time, clears laps and returns zero.
- [ ] Repeated start/pause/reset taps do not speed up or crash.
- [ ] Lap history scrolls; controls remain reachable on short/landscape screens.
- [ ] Compare with a separate phone/physical stopwatch and record duration, difference and device conditions. Do not assert perfect precision.
- [ ] TalkBack announces useful labels without continuous time spam.

Minute/hour formatting boundaries are covered by controlled-clock engine tests; waiting an hour on a physical device is not claimed.
