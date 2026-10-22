# Task 1 ko samjhein

## Conversion ka hisaab
Length ke liye base unit metre hai; weight ke liye kilogram. Har unit ka apna factor hai. Formula:

`result = input x sourceFactor / targetFactor`

Misal: 100 centimetres se metres: `100 x 0.01 / 1 = 1 metre`. 1 inch se centimetres: `1 x 0.0254 / 0.01 = 2.54 centimetres`. 1 kilogram se grams: `1 x 1 / 0.001 = 1000 grams`.

Code ratio pehle calculate karta hai, phir value se multiply karta hai taake kuch unnecessary intermediate overflows avoid hon. Hisaab unit ke numeric index/factor par hota hai; screen ke label ko parse karke formula nahi chalta.

Temperature mein pehle Celsius nikalte hain:
- Fahrenheit se Celsius: `(F - 32) / 1.8`.
- Kelvin se Celsius: `K - 273.15`.
- Celsius se Fahrenheit: `C x 1.8 + 32`.
- Celsius se Kelvin: `C + 273.15`.

Isi liye 0 Celsius = 32 Fahrenheit aur 273.15 Kelvin. Absolute zero -273.15 Celsius / -459.67 Fahrenheit / 0 Kelvin hai. Is se kam input invalid hai. Negative Celsius valid ho sakta hai, lekin negative length/weight reject hota hai.

## Har event handler ka kaam
- `onCreate`: XML screen load karta hai, controls dhoondta hai aur listeners jorta hai. System bars/keyboard ke liye padding bhi set hoti hai.
- Category `onItemSelected`: category badalne par relevant unit lists lagata hai; From pehla aur To doosra unit banata hai, purana result clear karta hai.
- Units `onItemSelected`: source ya target badalne par purana result hata deta hai, taake ghalat label ke saath purana number na dikhe.
- `TextWatcher.onTextChanged`: input badalte hi stale result aur purana field error clear karta hai.
- Convert click: `convert()` chalata hai. Empty/non-numeric/invalid range ko check karta hai; phir ConversionEngine se result mangta hai aur screen par unit ke saath dikhata hai. Invalid input par field error aur Toast aata hai.
- Swap click: From aur To ki selection exchange karta hai aur result clear karta hai. Nayi calculation ke liye Convert dabana hota hai.
- Keyboard Done: Convert button wala hi calculation method chalata hai.
- `onSaveInstanceState`: category, units, input aur result ko bundle mein rakhta hai. Restoration ke exact UI behavior ko rotation par abhi manual verify karna hai.
- `adapter()`: unit/category names ko Spinner mein lagane ka helper hai.
- `format()`: number ko readable banata hai; bohat bara/chhota result scientific notation mein, zero ko 0 dikhata hai.
- `clearResult()`: result card ko ready state mein laata hai.

## Asal test result
80 pure Java conversion checks aur Android 13 emulator par 30 Activity UI checks pass hue. UI checks mein real EditText, Spinners aur Convert/Swap click handlers use hue. Ye human touch, keyboard, TalkBack, all screen sizes aur rotation ki full verification ka replacement nahi hain.

## Video mein bolne ki seedhi example
"Main ne Java aur XML se Unit Converter banaya hai. XML layout dikhata hai aur Java input aur conversion handle karta hai. Length aur weight ke liye base-unit factors use kiye hain. Temperature ke liye alag formulas hain. Category badalne se units reset hote hain. Ghalat input par app crash karne ke bajaye error dikhati hai."
