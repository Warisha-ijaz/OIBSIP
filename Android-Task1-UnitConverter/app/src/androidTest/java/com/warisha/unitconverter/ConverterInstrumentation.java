package com.warisha.unitconverter;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.io.File;
import java.io.FileOutputStream;

/** Device checks through the real Activity; no external testing dependencies. */
public class ConverterInstrumentation extends Instrumentation {
    private Activity activity;
    private int count;
    @Override public void onCreate(Bundle args) { super.onCreate(args); start(); }
    private void ui(Runnable action) { runOnMainSync(action); waitForIdleSync(); }
    private Spinner spinner(int id) { return activity.findViewById(id); }
    private void category(int index) { ui(() -> spinner(R.id.category).setSelection(index)); }
    private void units(int from,int to) { ui(() -> {spinner(R.id.from).setSelection(from);spinner(R.id.to).setSelection(to);}); }
    private void input(String text) { ui(() -> ((EditText)activity.findViewById(R.id.value)).setText(text)); }
    private void convert() { ui(() -> activity.findViewById(R.id.convert).performClick()); }
    private void expect(boolean condition,String message) { if(!condition) throw new AssertionError(message); count++; }
    private String result() { final String[] s={""};ui(() -> s[0]=((TextView)activity.findViewById(R.id.result)).getText().toString());return s[0]; }
    private void example(int c,int from,int to,String input,String expected) {
        category(c);units(from,to);input(input);convert();expect(result().startsWith(expected+" "),"Expected "+expected+"; got "+result());
    }
    private void invalid(String value) {
        input(value);convert(); final boolean[] error={false};
        ui(() -> error[0]=((EditText)activity.findViewById(R.id.value)).getError()!=null);
        expect(error[0],"Missing field validation for "+value);expect(result().equals("Ready when you are"),"Stale result after invalid input");
    }
    private void screenshot(String name) throws Exception {
        android.graphics.Bitmap bitmap=getUiAutomation().takeScreenshot();
        File dir=new File(getTargetContext().getExternalFilesDir(null),"verification");dir.mkdirs();
        try(FileOutputStream stream=new FileOutputStream(new File(dir,name))) {bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG,100,stream);}
        bitmap.recycle();
    }
    @Override public void onStart() {
        Bundle result=new Bundle();
        try {
            Intent intent=new Intent(getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity=startActivitySync(intent);waitForIdleSync();
            example(0,1,2,"100","1"); screenshot("length.png");
            example(0,4,1,"1","2.54");
            example(1,2,1,"1","1000");screenshot("weight.png");
            example(2,0,1,"0","32");
            example(2,0,1,"100","212");
            example(2,0,2,"0","273.15");screenshot("temperature.png");
            example(2,0,1,"-40","-40");
            example(2,0,2,"-273.15","0");
            example(2,1,2,"-459.67","0");
            example(0,2,2,"123.5","123.5");
            example(0,2,3,"0","0");
            invalid("");invalid("-");invalid("-1");
            category(2);units(0,2);invalid("-274");units(1,2);invalid("-460");units(2,0);invalid("-1");
            category(0);units(3,0);input("1"+"0".repeat(99));convert();
            expect(result().startsWith("1E105 "),"Large finite input must remain valid");
            category(1);expect(result().equals("Ready when you are"),"Category must clear result");
            expect(spinner(R.id.from).getCount()==4,"Weight units not reset");
            expect(spinner(R.id.from).getSelectedItemPosition()==0 && spinner(R.id.to).getSelectedItemPosition()==1,"Category default units not reset");
            units(2,1);ui(() -> activity.findViewById(R.id.swap).performClick());
            expect(spinner(R.id.from).getSelectedItemPosition()==1 && spinner(R.id.to).getSelectedItemPosition()==2,"Swap failed");
            example(0,1,2,"100","1");
            ui(() -> {for(int i=0;i<40;i++)activity.findViewById(R.id.convert).performClick();});
            expect(result().startsWith("1 "),"Repeated conversion changed result");
            screenshot("final-length.png");
            result.putString("stream","PASS: "+count+" Activity UI checks; screenshots saved in external app files/verification.\n");
            finish(Activity.RESULT_OK,result);
        } catch(Throwable error) { result.putString("stream","FAIL: "+error.toString()+"\n");finish(Activity.RESULT_CANCELED,result); }
    }
}
