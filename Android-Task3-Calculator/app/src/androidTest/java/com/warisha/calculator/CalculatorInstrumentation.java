package com.warisha.calculator;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import java.io.*;

public class CalculatorInstrumentation extends Instrumentation {
    private Activity activity;
    private int count;
    @Override public void onCreate(Bundle args){super.onCreate(args);start();}
    private void ui(Runnable r){runOnMainSync(r);waitForIdleSync();}
    private int id(char key){
        if(Character.isDigit(key))return getTargetContext().getResources().getIdentifier("digit"+key,"id",getTargetContext().getPackageName());
        switch(key){case '+':return R.id.plus;case '-':return R.id.minus;case '*':return R.id.multiply;case '/':return R.id.divide;case '.':return R.id.decimal;case '=':return R.id.equals;case 'B':return R.id.backspace;case 'S':return R.id.sign;default:return R.id.clear;}
    }
    private void keys(String keys){ui(()->{for(char c:keys.toCharArray())activity.findViewById(id(c)).performClick();});}
    private String display(){final String[] text={""};ui(()->text[0]=((TextView)activity.findViewById(R.id.display)).getText().toString());return text[0];}
    private void expect(String value){if(!display().equals(value))throw new AssertionError(display()+" != "+value);count++;}
    private void test(String keys,String value){keys("C"+keys);expect(value);}
    private void screenshot(String name)throws Exception{
        android.graphics.Bitmap bitmap=getUiAutomation().takeScreenshot();File dir=new File(getTargetContext().getExternalFilesDir(null),"verification");dir.mkdirs();
        try(FileOutputStream out=new FileOutputStream(new File(dir,name))){bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG,100,out);}bitmap.recycle();
    }
    @Override public void onStart(){Bundle outcome=new Bundle();try{
        activity=startActivitySync(new Intent(getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));waitForIdleSync();
        test("12+8=","20");screenshot("addition.png");
        test("9-12=","-3");test("7*6=","42");test("8/2=","4");test("0.1+0.2=","0.3");screenshot("decimal.png");
        test("5/0=","Error");screenshot("error.png");keys("7");expect("7");
        test(".5+.5=","1");test("1..2","1.2");test("5+*2=","10");test("5+3*2=","16");
        test("5+=","5");keys("2=");expect("7");keys("==");expect("7");
        test("123B","12");keys("BBC");expect("0");test("2+3=9","9");test("2+3=*2=","10");test("2+S3=","-1");
        test("12345678901234567890","1234567890123456");screenshot("long-input.png");
        test("123+4","4");
        ActivityMonitor monitor=addMonitor(MainActivity.class.getName(),null,false);
        ui(()->activity.recreate());Activity recreated=waitForMonitorWithTimeout(monitor,10000);removeMonitor(monitor);
        if(recreated==null)throw new AssertionError("Activity recreation timed out");activity=recreated;waitForIdleSync();expect("4");keys("=");expect("127");
        ui(()->{for(int i=0;i<100;i++)for(char c:"C12+3=B".toCharArray())activity.findViewById(id(c)).performClick();});count++;
        test("12+8=","20");screenshot("final.png");
        outcome.putString("stream","PASS: "+count+" Activity UI checks, including Activity recreation and 100 rapid input sequences.\n");finish(Activity.RESULT_OK,outcome);
    }catch(Throwable e){outcome.putString("stream","FAIL: "+e+"\n");finish(Activity.RESULT_CANCELED,outcome);}}
}
