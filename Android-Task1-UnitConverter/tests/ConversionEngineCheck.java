import com.warisha.unitconverter.ConversionEngine;
public class ConversionEngineCheck {
    static int count;
    static void check(int c,int f,int t,double value,double expected) {
        double actual=ConversionEngine.convert(c,f,t,value);
        if(Math.abs(actual-expected)>1e-8*Math.max(1,Math.abs(expected))) throw new AssertionError(actual+" != "+expected);
        count++;
    }
    static void reject(int c,int f,double value) {
        try {ConversionEngine.convert(c,f,0,value);} catch(IllegalArgumentException e){count++;return;}
        throw new AssertionError("Accepted invalid input");
    }
    public static void main(String[] args) {
        check(0,1,2,100,1);check(0,4,1,1,2.54);check(1,2,1,1,1000);
        check(2,0,1,0,32);check(2,0,1,100,212);check(2,0,2,0,273.15);
        check(2,2,0,0,-273.15);check(2,1,2,-459.67,0);check(0,2,2,7,7);
        check(0,0,3,0,0);reject(0,0,-1);reject(1,0,-1);reject(2,0,-274);
        reject(2,1,-460);reject(2,2,-1);reject(0,0,Double.NaN);reject(0,0,Double.POSITIVE_INFINITY);
        check(2,1,1,Double.MAX_VALUE,Double.MAX_VALUE);
        try { ConversionEngine.convert(0,3,0,Double.MAX_VALUE); throw new AssertionError("Overflow accepted"); }
        catch(IllegalArgumentException expected) { count++; }
        for(int c=0;c<3;c++)for(int f=0;f<ConversionEngine.UNITS[c].length;f++)for(int t=0;t<ConversionEngine.UNITS[c].length;t++) {
            double n=ConversionEngine.convert(c,f,t,42);
            check(c,t,f,n,42);
        }
        System.out.println("PASS: "+count+" conversion checks");
    }
}
