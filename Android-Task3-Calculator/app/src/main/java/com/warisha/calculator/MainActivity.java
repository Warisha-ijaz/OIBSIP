package com.warisha.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

public class MainActivity extends Activity {
    private CalculatorEngine engine;
    private TextView display,expression,message;
    @Override public void onCreate(Bundle state){
        super.onCreate(state);setContentView(R.layout.activity_main);
        engine=state!=null && state.getSerializable("engine") instanceof CalculatorEngine ? (CalculatorEngine)state.getSerializable("engine") : new CalculatorEngine();
        View root=findViewById(R.id.root);
        root.setOnApplyWindowInsetsListener((v,insets)->{
            if(android.os.Build.VERSION.SDK_INT>=30){android.graphics.Insets bars=insets.getInsets(WindowInsets.Type.systemBars());v.setPadding(bars.left,bars.top,bars.right,bars.bottom);}
            else v.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());
            return insets;
        });
        display=findViewById(R.id.display);expression=findViewById(R.id.expression);message=findViewById(R.id.message);
        int[] digits={R.id.digit0,R.id.digit1,R.id.digit2,R.id.digit3,R.id.digit4,R.id.digit5,R.id.digit6,R.id.digit7,R.id.digit8,R.id.digit9};
        for(int i=0;i<digits.length;i++){final char digit=(char)('0'+i);findViewById(digits[i]).setOnClickListener(v->{engine.digit(digit);render();});}
        bind(R.id.clear,()->engine.clear());bind(R.id.backspace,()->engine.backspace());
        bind(R.id.sign,()->engine.sign());bind(R.id.decimal,()->engine.decimal());bind(R.id.equals,()->engine.equals());
        bind(R.id.plus,()->engine.operator("+"));bind(R.id.minus,()->engine.operator("-"));
        bind(R.id.multiply,()->engine.operator("*"));bind(R.id.divide,()->engine.operator("/"));render();
    }
    private void bind(int id,Runnable action){findViewById(id).setOnClickListener(v->{action.run();render();});}
    private void render(){display.setText(engine.display());expression.setText(engine.expression());message.setText(engine.message());display.setTextColor(getColor(engine.isError()?R.color.error:R.color.ink));}
    @Override public void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);out.putSerializable("engine",engine);}
}
