package com.warisha.stopwatch;

import android.app.Activity;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
    private StopwatchEngine engine;
    private final Handler handler=new Handler(Looper.getMainLooper());
    private boolean visible;
    private TextView time,fraction,status,count,empty;
    private Button start,pause,lap,reset;
    private LinearLayout rows;
    private final Runnable ticker=new Runnable(){public void run(){renderTime();if(visible&&engine.isRunning())handler.postDelayed(this,50);}};
    @Override public void onCreate(Bundle saved){
        super.onCreate(saved);setContentView(R.layout.activity_main);
        engine=saved!=null&&saved.getSerializable("engine") instanceof StopwatchEngine?(StopwatchEngine)saved.getSerializable("engine"):new StopwatchEngine();
        // Saved instance state is restored on the same boot only. No durable preferences are used.
        if(saved!=null&&saved.getInt("boot",-1)!=bootCount())engine.reset();
        View root=findViewById(R.id.root);
        root.setOnApplyWindowInsetsListener((v,insets)->{
            if(Build.VERSION.SDK_INT>=30){android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(bars.left,bars.top,bars.right,bars.bottom);}
            else v.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());return insets;
        });
        time=findViewById(R.id.time);fraction=findViewById(R.id.fraction);status=findViewById(R.id.status);count=findViewById(R.id.lapCount);empty=findViewById(R.id.empty);rows=findViewById(R.id.lapRows);
        start=findViewById(R.id.start);pause=findViewById(R.id.pause);lap=findViewById(R.id.lap);reset=findViewById(R.id.reset);
        start.setOnClickListener(v->{engine.start(SystemClock.elapsedRealtime());renderControls();schedule();});
        pause.setOnClickListener(v->{engine.pause(SystemClock.elapsedRealtime());handler.removeCallbacks(ticker);renderControls();});
        reset.setOnClickListener(v->{handler.removeCallbacks(ticker);engine.reset();renderControls();renderLaps();});
        lap.setOnClickListener(v->{if(engine.addLap(SystemClock.elapsedRealtime())){renderLaps();renderControls();}else Toast.makeText(this,R.string.limit,Toast.LENGTH_SHORT).show();});
        renderControls();renderLaps();
    }
    private int bootCount(){return android.provider.Settings.Global.getInt(getContentResolver(),android.provider.Settings.Global.BOOT_COUNT,-1);}
    private void schedule(){handler.removeCallbacks(ticker);if(visible&&engine.isRunning())handler.post(ticker);}
    private void renderTime(){long elapsed=engine.elapsed(SystemClock.elapsedRealtime());time.setText(StopwatchEngine.format(elapsed));fraction.setText(getString(R.string.hundredths,(elapsed/10)%100));time.setContentDescription("Elapsed time "+StopwatchEngine.precise(elapsed));}
    private void enabled(Button button,boolean enabled){button.setEnabled(enabled);button.setAlpha(enabled?1f:0.4f);}
    private void renderControls(){
        renderTime();boolean running=engine.isRunning();long elapsed=engine.elapsed(SystemClock.elapsedRealtime());
        enabled(start,!running);enabled(pause,running);enabled(lap,running&&engine.laps().size()<StopwatchEngine.MAX_LAPS);enabled(reset,running||elapsed>0||!engine.laps().isEmpty());
        start.setText(!running&&elapsed>0?R.string.resume:R.string.start);status.setText(running?R.string.running:elapsed>0?R.string.paused:R.string.ready);
    }
    private void renderLaps(){
        rows.removeAllViews();count.setText(getString(R.string.lap_count,engine.laps().size()));empty.setVisibility(engine.laps().isEmpty()?View.VISIBLE:View.GONE);
        for(int i=engine.laps().size()-1;i>=0;i--){
            long total=engine.laps().get(i),previous=i==0?0:engine.laps().get(i-1);
            TextView row=new TextView(this);row.setText(getString(R.string.lap_name,i+1)+"     "+StopwatchEngine.precise(total)+"\nSplit  "+StopwatchEngine.precise(total-previous));
            row.setTextSize(14);row.setTextColor(getColor(R.color.ink));int pad=(int)(12*getResources().getDisplayMetrics().density);row.setPadding(0,pad,0,pad);rows.addView(row);
        }
    }
    @Override protected void onResume(){super.onResume();visible=true;renderControls();schedule();}
    @Override protected void onPause(){visible=false;handler.removeCallbacks(ticker);super.onPause();}
    @Override protected void onDestroy(){handler.removeCallbacks(ticker);super.onDestroy();}
    @Override protected void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);out.putSerializable("engine",engine);out.putInt("boot",bootCount());}
}
