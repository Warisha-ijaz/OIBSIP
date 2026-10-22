package com.warisha.stopwatch;
import android.app.*;
import android.content.Intent;
import android.os.*;
import android.widget.*;
import java.io.*;

public class StopwatchInstrumentation extends Instrumentation {
    private Activity activity;
    private int checks;
    private String comparison="";
    @Override public void onCreate(Bundle args){super.onCreate(args);start();}
    private void ui(Runnable r){runOnMainSync(r);waitForIdleSync();}
    private void click(int id){ui(()->activity.findViewById(id).performClick());}
    private void expect(boolean yes,String why){if(!yes)throw new AssertionError(why);checks++;}
    private boolean enabled(int id){final boolean[] b={false};ui(()->b[0]=activity.findViewById(id).isEnabled());return b[0];}
    private long displayed(){final String[] s={"",""};ui(()->{s[0]=((TextView)activity.findViewById(R.id.time)).getText().toString();s[1]=((TextView)activity.findViewById(R.id.fraction)).getText().toString();});String[] parts=s[0].split(":");return (Long.parseLong(parts[0])*3600+Long.parseLong(parts[1])*60+Long.parseLong(parts[2]))*1000+Long.parseLong(s[1].substring(1))*10;}
    private int laps(){final int[] n={0};ui(()->n[0]=((LinearLayout)activity.findViewById(R.id.lapRows)).getChildCount());return n[0];}
    private void screenshot(String name)throws Exception{android.graphics.Bitmap bitmap=getUiAutomation().takeScreenshot();File dir=new File(getTargetContext().getExternalFilesDir(null),"verification");dir.mkdirs();try(FileOutputStream out=new FileOutputStream(new File(dir,name))){bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG,100,out);}bitmap.recycle();}
    @Override public void onStart(){Bundle result=new Bundle();try{
        activity=startActivitySync(new Intent(getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));waitForIdleSync();
        click(R.id.reset);expect(displayed()==0,"Initial zero");expect(enabled(R.id.start),"Start enabled initially");expect(!enabled(R.id.pause),"Pause disabled initially");expect(!enabled(R.id.lap),"Lap disabled initially");screenshot("ready.png");
        long referenceStart=System.nanoTime();click(R.id.start);expect(!enabled(R.id.start),"Start disabled running");expect(enabled(R.id.pause),"Pause enabled running");expect(enabled(R.id.lap),"Lap enabled running");
        final boolean[] runningLabel={false};ui(()->runningLabel[0]=((Button)activity.findViewById(R.id.start)).getText().toString().equals("Start"));expect(runningLabel[0],"Disabled running button label");
        SystemClock.sleep(1200);click(R.id.pause);long referenceMillis=(System.nanoTime()-referenceStart)/1000000;long paused=displayed();
        expect(Math.abs(paused-referenceMillis)<500,"Time diverged from nanoTime comparison");comparison="Pause comparison: UI="+paused+" ms; nanoTime interval="+referenceMillis+" ms.";
        expect(paused>=1000,"Timer did not advance");SystemClock.sleep(400);expect(displayed()==paused,"Paused display changed");
        expect(enabled(R.id.start),"Resume enabled");expect(!enabled(R.id.pause),"Pause disabled after pause");
        click(R.id.start);SystemClock.sleep(400);click(R.id.lap);expect(laps()==1,"First lap missing");SystemClock.sleep(250);click(R.id.lap);expect(laps()==2,"Second lap missing");screenshot("running-laps.png");click(R.id.pause);expect(displayed()>paused,"Resume did not accumulate");
        ActivityMonitor monitor=addMonitor(MainActivity.class.getName(),null,false);long beforeRecreate=displayed();ui(()->activity.recreate());Activity next=waitForMonitorWithTimeout(monitor,10000);removeMonitor(monitor);
        if(next==null)throw new AssertionError("Recreation timed out");activity=next;waitForIdleSync();expect(displayed()==beforeRecreate,"Paused time lost during recreation");expect(laps()==2,"Laps lost during recreation");expect(enabled(R.id.start),"Resume disabled after recreation");
        click(R.id.start);long beforeHome=displayed();long homeReference=System.nanoTime();
        Intent home=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);getTargetContext().startActivity(home);waitForIdleSync();SystemClock.sleep(10000);
        getTargetContext().startActivity(new Intent(getTargetContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));waitForIdleSync();SystemClock.sleep(250);
        long afterHome=displayed();long homeMillis=(System.nanoTime()-homeReference)/1000000;expect(afterHome-beforeHome>=9900,"Background interval not included");expect(Math.abs((afterHome-beforeHome)-homeMillis)<750,"Background comparison out of tolerance");comparison+=" Background: UI advance="+(afterHome-beforeHome)+" ms; nanoTime="+homeMillis+" ms.";
        click(R.id.pause);screenshot("paused-after-background.png");
        monitor=addMonitor(MainActivity.class.getName(),null,false);click(R.id.start);long runningBefore=displayed();ui(()->activity.recreate());next=waitForMonitorWithTimeout(monitor,10000);removeMonitor(monitor);if(next==null)throw new AssertionError("Running recreation timeout");activity=next;waitForIdleSync();SystemClock.sleep(200);expect(displayed()>=runningBefore,"Running time lost on recreation");expect(!enabled(R.id.start),"Running state lost");
        click(R.id.reset);expect(displayed()==0,"Reset not zero");expect(laps()==0,"Reset did not clear laps");expect(!enabled(R.id.reset),"Reset should be disabled at zero");
        ui(()->{for(int i=0;i<100;i++){activity.findViewById(R.id.start).performClick();activity.findViewById(R.id.start).performClick();activity.findViewById(R.id.pause).performClick();activity.findViewById(R.id.pause).performClick();activity.findViewById(R.id.reset).performClick();}});expect(displayed()==0,"Repeated taps changed reset state");
        screenshot("final.png");result.putString("stream","PASS: "+checks+" Activity UI checks, including 10-second Home/return, recreation and 100 repeated tap sequences. "+comparison+"\n");finish(Activity.RESULT_OK,result);
    }catch(Throwable e){result.putString("stream","FAIL: "+e+"; "+comparison+"\n");finish(Activity.RESULT_CANCELED,result);}}
}
