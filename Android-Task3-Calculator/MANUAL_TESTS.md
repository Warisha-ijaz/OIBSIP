# Manual acceptance checklist

Programmatic Activity checks are reported separately in VERIFICATION.md. These boxes are for human checks on the selected device; do not mark them complete without observing results.

- [ ] 12 + 8 = 20
- [ ] 9 - 12 = -3
- [ ] 7 x 6 = 42
- [ ] 8 / 2 = 4
- [ ] 0.1 + 0.2 = 0.3
- [ ] 5 / 0 shows Error; pressing 7 recovers to 7.
- [ ] Decimal first produces 0.; second dot is ignored.
- [ ] 5 + x 2 = 10 (pending operator replaced).
- [ ] 5 + 3 x 2 = 16 (left-to-right).
- [ ] 5 + = leaves 5 pending; entering 2 = produces 7.
- [ ] Repeated equals leaves answer unchanged.
- [ ] Backspace edits the current number; C clears everything.
- [ ] Digits after a result start over; operator continues from answer.
- [ ] Long input remains readable; extra digits give feedback.
- [ ] Rapid taps do not crash the app.
- [ ] Rotate during 123 + 4, then press equals: answer 127.
- [ ] Landscape/short screen scrolls; all keys remain reachable.
- [ ] TalkBack labels make operators and backspace understandable.
- [ ] Background/return leaves the calculation usable.
