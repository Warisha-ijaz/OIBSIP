package com.warisha.stopwatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Time comes from the caller's monotonic clock, never from UI tick counts. */
public final class StopwatchEngine implements Serializable {
    private static final long serialVersionUID=1L;
    public static final int MAX_LAPS=200;
    private long accumulated, startedAt;
    private boolean running;
    private final ArrayList<Long> laps=new ArrayList<>();
    public void start(long now){if(running)return;startedAt=now;running=true;}
    public void pause(long now){if(!running)return;accumulated=elapsed(now);running=false;}
    public void reset(){accumulated=0;startedAt=0;running=false;laps.clear();}
    public long elapsed(long now){
        if(!running)return accumulated;
        long delta=Math.max(0,now-startedAt);
        return delta>Long.MAX_VALUE-accumulated?Long.MAX_VALUE:accumulated+delta;
    }
    public boolean addLap(long now){if(!running||laps.size()>=MAX_LAPS)return false;laps.add(elapsed(now));return true;}
    public List<Long> laps(){return Collections.unmodifiableList(laps);}
    public boolean isRunning(){return running;}
    public static String format(long millis){long seconds=Math.max(0,millis)/1000;return String.format(Locale.US,"%02d:%02d:%02d",seconds/3600,(seconds/60)%60,seconds%60);}
    public static String precise(long millis){return format(millis)+String.format(Locale.US,".%02d",(Math.max(0,millis)/10)%100);}
}
