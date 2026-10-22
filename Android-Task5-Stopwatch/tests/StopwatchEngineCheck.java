import com.warisha.stopwatch.StopwatchEngine;
import java.io.*;
public class StopwatchEngineCheck {
    static int checks;
    static void expect(boolean yes){if(!yes)throw new AssertionError("Check "+(checks+1));checks++;}
    public static void main(String[] args)throws Exception{
        StopwatchEngine e=new StopwatchEngine();expect(e.elapsed(1000)==0);expect(!e.isRunning());expect(!e.addLap(0));
        e.start(1000);expect(e.isRunning());expect(e.elapsed(2500)==1500);e.start(2000);expect(e.elapsed(2500)==1500);
        e.pause(3000);expect(!e.isRunning());expect(e.elapsed(9000)==2000);e.pause(10000);expect(e.elapsed(10000)==2000);
        e.start(12000);expect(e.elapsed(13000)==3000);expect(e.addLap(13000));expect(e.laps().get(0)==3000);
        expect(e.addLap(14500));expect(e.laps().get(1)==4500);
        e.pause(15000);expect(e.elapsed(99999)==5000);expect(!e.addLap(99999));
        e.reset();expect(e.elapsed(100000)==0);expect(e.laps().isEmpty());expect(!e.isRunning());
        expect(StopwatchEngine.format(59999).equals("00:00:59"));expect(StopwatchEngine.format(60000).equals("00:01:00"));
        expect(StopwatchEngine.format(3599999).equals("00:59:59"));expect(StopwatchEngine.format(3600000).equals("01:00:00"));
        expect(StopwatchEngine.format(360000000).equals("100:00:00"));expect(StopwatchEngine.precise(12345).equals("00:00:12.34"));
        e.start(100);for(int i=0;i<200;i++)expect(e.addLap(100+i));expect(!e.addLap(400));expect(e.laps().size()==200);
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();new ObjectOutputStream(bytes).writeObject(e);
        e=(StopwatchEngine)new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();expect(e.elapsed(10100)==10000);expect(e.laps().size()==200);
        e.reset();for(int i=0;i<1000;i++){e.start(i*10);e.start(i*10+1);e.pause(i*10+5);e.pause(i*10+6);}expect(e.elapsed(100000)==5000);
        e.reset();expect(e.elapsed(Long.MAX_VALUE)==0);
        System.out.println("PASS: "+checks+" engine checks, including format boundaries, laps and 1000 repeated start/pause sequences");
    }
}
