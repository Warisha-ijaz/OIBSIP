# Calculator ki logic samjhein

`CalculatorEngine` hisaab aur input ki state rakhta hai. `MainActivity` buttons ko engine se jorti hai aur screen par us ka result dikhati hai. XML sirf layout aur styles define karta hai.

## State kya hoti hai?
- `entry`: jo number abhi type ho raha hai.
- `accumulator`: pehla number ya ab tak ka answer.
- `pending`: agla operation, jaise + ya multiply.
- `hasEntry`: kya right side ka number type kiya gaya hai?
- `resultShown`: kya equals ka answer dikh raha hai?
- `error`: kya invalid calculation hui hai?
- `history`: display ke upar chhoti expression line.

Misal `12 + 8 =`: pehle entry 12 hoti hai. Plus par accumulator 12 aur pending + ho jata hai. 8 dabane par nayi entry 8 banti hai. Equals par 12 + 8 calculate hota hai, answer 20 aur pending operation khali ho jata hai.

## Har button ka kaam
- Digit: active number mein digit add karta hai. Result ke baad digit nayi calculation shuru karta hai.
- Decimal: sirf ek dot allow karta hai; empty number par 0. banata hai.
- Operator: zarurat par pichla operation solve karta hai, warna pending operator replace karta hai.
- Equals: dono operands hon to calculation karta hai. Right operand missing ho ya equals repeat ho to answer change nahi hota.
- Clear: tamam state reset karke 0 dikhata hai.
- Backspace: active input ka aakhri character hatata hai. Result/error ke baad poori calculation clear karta hai.
- Sign: positive/negative sign badalta hai.
- `render`: engine ki display, expression aur message TextViews mein lagata hai; Error ko red dikhata hai.
- `onSaveInstanceState`: engine save karta hai; Activity dobara banne par wohi calculation wapas milti hai.

## Left-to-right ka matlab
`5 + 3 x 2` mein multiply dabate waqt pehle 5 + 3 = 8 hota hai. Phir 8 x 2 = 16. App scientific precedence wala calculator nahi hai; ye choice screen aur README mein clear hai.

## Decimal aur errors
BigDecimal use kiya hai, is liye 0.1 + 0.2 ka answer 0.3 hota hai. Division aur long results 16 significant digits tak round hotay hain. Zero se divide karne par Error dikhata hai. New digit ya Clear se recover hota hai. Bohat bare results supported range se bahar hon to friendly error aata hai.

## Demo narration
"Ye meri Java aur XML Calculator app hai. Is mein four basic operations, decimals, clear, backspace aur sign toggle hain. Calculation left-to-right hoti hai. Zero se divide karne par Error aata hai aur main naya number dal kar recover kar sakti hoon. Screen rotate karne par calculation save rehti hai."
