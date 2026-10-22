package com.warisha.calculator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/** Immediate execution, decimal arithmetic, and explicit input state. */
public final class CalculatorEngine implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final MathContext MC = new MathContext(16, RoundingMode.HALF_UP);
    private static final BigDecimal LIMIT = BigDecimal.ONE.scaleByPowerOfTen(100);
    private String entry = "0", pending = "", history = "", message = "";
    private BigDecimal accumulator;
    private boolean hasEntry = true, resultShown, error;

    public void clear() {
        entry="0"; pending=""; history=""; message=""; accumulator=null;
        hasEntry=true;resultShown=false;error=false;
    }
    public void digit(char digit) {
        if (digit<'0'||digit>'9') return;
        if(error || (resultShown && pending.isEmpty())) clear();
        message="";
        if(!hasEntry) {entry="0";hasEntry=true;}
        if (entry.replace("-", "").replace(".", "").length()>=16 && !entry.equals("0") && !entry.equals("-0")) {
            message="Maximum 16 input digits";return;
        }
        if(entry.equals("0")) entry=String.valueOf(digit);
        else if(entry.equals("-0")) entry="-"+digit;
        else entry+=digit;
        resultShown=false;
    }
    public void decimal() {
        if(error || (resultShown && pending.isEmpty())) clear();
        message="";
        if(!hasEntry){entry="0";hasEntry=true;}
        if(!entry.contains("."))entry+=".";
        resultShown=false;
    }
    public void sign() {
        if(error)clear();
        message="";
        if(!hasEntry){entry="0";hasEntry=true;}
        entry=entry.startsWith("-")?entry.substring(1):"-"+entry;
        if(resultShown){accumulator=number();history="";}
    }
    public void backspace() {
        if(error || resultShown){clear();return;}
        message="";
        if(!hasEntry)return;
        entry=entry.length()>1?entry.substring(0,entry.length()-1):"0";
        if(entry.equals("-"))entry="0";
    }
    public void operator(String op) {
        if(!op.equals("+")&&!op.equals("-")&&!op.equals("*")&&!op.equals("/"))return;
        if(error)return;
        message="";
        try {
            if(accumulator!=null && !pending.isEmpty() && hasEntry) accumulator=calculate(accumulator,number(),pending);
            else if(accumulator==null || pending.isEmpty())accumulator=number();
            entry=format(accumulator);pending=op;hasEntry=false;resultShown=false;
            history=entry+" "+symbol(op);
        }catch(ArithmeticException e){fail(e.getMessage());}
    }
    public void equals() {
        if(error)return;
        message="";
        // Missing right operand and repeated equals deliberately leave state unchanged.
        if(pending.isEmpty() || !hasEntry)return;
        try {
            BigDecimal right=number();String expression=format(accumulator)+" "+symbol(pending)+" "+format(right)+" =";
            accumulator=calculate(accumulator,right,pending);entry=format(accumulator);
            pending="";history=expression;hasEntry=true;resultShown=true;
        }catch(ArithmeticException e){fail(e.getMessage());}
    }
    private BigDecimal number(){return new BigDecimal(entry);}
    private BigDecimal calculate(BigDecimal a,BigDecimal b,String op){
        BigDecimal value;
        switch(op){
            case "+":value=a.add(b,MC);break;
            case "-":value=a.subtract(b,MC);break;
            case "*":value=a.multiply(b,MC);break;
            case "/":if(b.signum()==0)throw new ArithmeticException("Cannot divide by zero");value=a.divide(b,MC);break;
            default:throw new ArithmeticException("Unknown operation");
        }
        if(value.abs().compareTo(LIMIT)>=0)throw new ArithmeticException("Result is too large");
        return value;
    }
    private void fail(String reason){entry="0";accumulator=null;pending="";history="";message=reason;error=true;hasEntry=true;resultShown=false;}
    public String display(){return error?"Error":entry.equals("-0")?"0":entry;}
    public String expression(){return history;}
    public String message(){return message;}
    public boolean isError(){return error;}
    private static String symbol(String op){return op.equals("*")?"×":op.equals("/")?"÷":op.equals("-")?"−":op;}
    private static String format(BigDecimal value){
        if(value.signum()==0)return "0";
        BigDecimal clean=value.stripTrailingZeros();
        return clean.abs().compareTo(new BigDecimal("1E16"))>=0 || clean.abs().compareTo(new BigDecimal("0.000000001"))<0 ? clean.toString() : clean.toPlainString();
    }
}
