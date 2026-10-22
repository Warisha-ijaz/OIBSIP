import com.warisha.calculator.CalculatorEngine;
import java.io.*;
public class CalculatorEngineCheck {
    static int count;
    static CalculatorEngine e=new CalculatorEngine();
    static void keys(String sequence){for(char c:sequence.toCharArray()){if(Character.isDigit(c))e.digit(c);else switch(c){case '.':e.decimal();break;case '=':e.equals();break;case 'C':e.clear();break;case 'B':e.backspace();break;case 'S':e.sign();break;default:e.operator(""+c);}}}
    static void test(String keys,String expected){e.clear();keys(keys);expect(expected);}
    static void expect(String expected){if(!e.display().equals(expected))throw new AssertionError(e.display()+" != "+expected);count++;}
    public static void main(String[] args)throws Exception{
        test("12+8=","20");test("9-12=","-3");test("7*6=","42");test("8/2=","4");
        test("0.1+0.2=","0.3");test("5/0=","Error");keys("7");expect("7");
        test(".5+.5=","1");test("1..2","1.2");test("5+*2=","10");test("5+3*2=","16");
        test("5+=","5");keys("2=");expect("7");keys("==");expect("7");
        test("12B","1");keys("B");expect("0");test("1+2=C","0");
        test("2+3=9","9");test("2+3=*2=","10");test("3S+2=","-1");
        test("2+S3=","-1");test("0S=","0");test("1/3=","0.3333333333333333");
        test("0005","5");test("12345678901234567890","1234567890123456");
        test("9*9=", "81"); for(int i=0;i<110 && !e.isError();i++)keys("*9=");expect("Error");keys("C");expect("0");
        test("5*0=","0");test("5-5=","0");test("123+4", "4");
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();new ObjectOutputStream(bytes).writeObject(e);
        e=(CalculatorEngine)new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();keys("=");expect("127");
        e.clear();for(int i=0;i<500;i++)keys("1+2=CB..");count++;
        System.out.println("PASS: "+count+" engine checks (including 500 repeated input sequences)");
    }
}
