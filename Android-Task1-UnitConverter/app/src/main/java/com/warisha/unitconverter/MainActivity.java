package com.warisha.unitconverter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends Activity {
    private Spinner category, from, to;
    private EditText value;
    private TextView result, detail;
    private int activeCategory = -1;
    private Bundle restore;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        View root = findViewById(R.id.root);
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            if (android.os.Build.VERSION.SDK_INT >= 30) {
                android.graphics.Insets bars = insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.ime());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            } else {
                v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            }
            return insets;
        });
        category = findViewById(R.id.category); from = findViewById(R.id.from); to = findViewById(R.id.to);
        value = findViewById(R.id.value); result = findViewById(R.id.result); detail = findViewById(R.id.detail);
        restore = state;
        category.setAdapter(adapter(ConversionEngine.CATEGORIES));
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> parent) {}
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (activeCategory == position) return;
                activeCategory = position;
                from.setAdapter(adapter(ConversionEngine.UNITS[position]));
                to.setAdapter(adapter(ConversionEngine.UNITS[position]));
                to.setSelection(1);
                clearResult();
                if (restore != null) {
                    from.setSelection(restore.getInt("from",0)); to.setSelection(restore.getInt("to",1));
                    value.setText(restore.getString("input",""));
                    result.setText(restore.getString("result",getString(R.string.empty_result)));
                    detail.setText(restore.getString("detail",getString(R.string.help))); restore = null;
                }
            }
        });
        if (state != null) category.setSelection(state.getInt("category",0));
        AdapterView.OnItemSelectedListener unitListener = new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> p) {}
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) { clearResult(); }
        };
        from.setOnItemSelectedListener(unitListener); to.setOnItemSelectedListener(unitListener);
        value.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
            public void onTextChanged(CharSequence s,int start,int before,int count) { clearResult(); value.setError(null); }
            public void afterTextChanged(android.text.Editable s) {}
        });
        findViewById(R.id.convert).setOnClickListener(v -> convert());
        findViewById(R.id.swap).setOnClickListener(v -> { int old = from.getSelectedItemPosition(); from.setSelection(to.getSelectedItemPosition()); to.setSelection(old); clearResult(); });
        value.setOnEditorActionListener((v, action, event) -> { if (action == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) { convert(); return true; } return false; });
    }
    private ArrayAdapter<String> adapter(String[] units) {
        ArrayAdapter<String> a = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); return a;
    }
    private void clearResult() { if (result != null) { result.setText(R.string.empty_result); detail.setText(R.string.help); } }
    private String format(double n) {
        if (n == 0) return "0";
        String pattern = Math.abs(n) >= 1e12 || Math.abs(n) < 1e-6 ? "0.##########E0" : "0.##########";
        return new DecimalFormat(pattern,DecimalFormatSymbols.getInstance(Locale.US)).format(n);
    }
    private void convert() {
        String input = value.getText().toString().trim();
        try {
            if (input.isEmpty()) throw new IllegalArgumentException("Please enter a value.");
            double n;
            try { n = Double.parseDouble(input); } catch (NumberFormatException e) { throw new IllegalArgumentException("Please enter a valid number."); }
            int c = category.getSelectedItemPosition(), f = from.getSelectedItemPosition(), t = to.getSelectedItemPosition();
            double converted = ConversionEngine.convert(c,f,t,n);
            String[] units = ConversionEngine.UNITS[c];
            result.setText(format(converted)+" "+units[t]);
            detail.setText(format(n)+" "+units[f]+" = "+format(converted)+" "+units[t]);
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(value.getWindowToken(),0);
        } catch (IllegalArgumentException e) {
            clearResult(); value.setError(e.getMessage()); Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out); out.putInt("category",category.getSelectedItemPosition());
        out.putInt("from",from.getSelectedItemPosition()); out.putInt("to",to.getSelectedItemPosition());
        out.putString("input",value.getText().toString()); out.putString("result",result.getText().toString()); out.putString("detail",detail.getText().toString());
    }
}
