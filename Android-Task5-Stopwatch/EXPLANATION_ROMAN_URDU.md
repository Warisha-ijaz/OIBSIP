# Stopwatch asaan alfaaz mein

## elapsedRealtime kya hai?
`SystemClock.elapsedRealtime()` phone ke boot hone ke baad guzray milliseconds deta hai. Date/time ki setting badalne se ye clock peeche nahi jati. Is mein phone ki sleep ka waqt bhi include hota hai.

## Accumulated time kya hai?
Ye woh waqt hai jo pehle run ho chuka hai. Misal: 5 seconds par Pause kiya, to accumulated time 5 seconds save hota hai. Resume par naya start timestamp milta hai; phir 3 seconds chalayein to total 5 + 3 = 8 seconds hota hai.

Formula: `total = pehle ka saved time + abhi ka clock - start ka clock`.

## Handler aur callback ka kya kaam hai?
Handler har takreeban 50 milliseconds par screen update karta hai. Callback woh chhota Runnable hai jo time TextViews mein dikhata hai. Ye total mein 50 milliseconds jorta nahi: har dafa clock se actual elapsed calculate karta hai. Is liye screen thori dair busy ho to timer permanently slow nahi hota.

## App se bahar jane par kya hota hai?
`onPause` screen update callback hata deta hai, lekin running timer ka timestamp aur state waisa hi rehta hai. `onResume` wapas latest time calculate karta hai aur screen updates shuru karta hai. Koi background service ya notification ki zaroorat nahi, kyunke sirf elapsed difference chahiye.

## Har button aur lifecycle handler
- Start: abhi ka timestamp save karta hai; double Start ignore hota hai.
- Pause: current elapsed save karke running false karta hai; screen updates stop hotay hain.
- Resume: accumulated time rakhta hai, naya timestamp leta hai.
- Reset: running false, elapsed zero, callbacks remove aur lap list clear.
- Lap: abhi ka total elapsed aur split history mein add karta hai; timer chalta rehta hai.
- onCreate: saved state ho to restore, screen bind aur listeners set karta hai.
- onResume/onPause: visible screen updates start/stop karte hain.
- onDestroy: purani Activity ke callbacks remove karta hai.
- onSaveInstanceState: engine aur boot count Bundle mein save karta hai. Rotation par nayi Activity ye state leti hai.

## Buttons grey kyun hotay hain?
Running state mein Start disabled aur Pause/Lap active hotay hain. Paused state mein Resume active aur Pause/Lap disabled hotay hain. Reset zero state mein disabled hai. Is se user ko pata chalta hai kaunsa action ab useful hai.

## Limit
App ko force-stop karna ya fresh task start karna session reset kar sakta hai. 200 laps ki limit hai. Display ke hundredths screen refresh ke saath badalte hain; perfect scientific accuracy ka claim nahi.
