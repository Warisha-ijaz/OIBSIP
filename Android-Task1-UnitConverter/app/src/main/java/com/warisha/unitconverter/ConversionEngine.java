package com.warisha.unitconverter;

/** Pure conversion math, independent of the Android screen. */
public final class ConversionEngine {
    public static final String[] CATEGORIES = {"Length", "Weight", "Temperature"};
    public static final String[][] UNITS = {
        {"Millimetres (mm)", "Centimetres (cm)", "Metres (m)", "Kilometres (km)", "Inches (in)", "Feet (ft)"},
        {"Milligrams (mg)", "Grams (g)", "Kilograms (kg)", "Pounds (lb)"},
        {"Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"}
    };
    private static final double[][] FACTORS = {{0.001,0.01,1,1000,0.0254,0.3048},{0.000001,0.001,1,0.45359237}};
    private ConversionEngine() {}
    public static double convert(int category, int from, int to, double value) {
        if (category < 0 || category >= UNITS.length || from < 0 || to < 0 || from >= UNITS[category].length || to >= UNITS[category].length)
            throw new IllegalArgumentException("Choose valid units.");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Enter a finite number.");
        double result;
        if (category < 2) {
            if (value < 0) throw new IllegalArgumentException("Length and weight cannot be negative.");
            result = from == to ? value : value * (FACTORS[category][from] / FACTORS[category][to]);
        } else {
            double minimum = from == 0 ? -273.15 : from == 1 ? -459.67 : 0;
            if (value < minimum) throw new IllegalArgumentException("Temperature cannot be below absolute zero.");
            double celsius = from == 0 ? value : from == 1 ? (value - 32) / 1.8 : value - 273.15;
            result = from == to ? value : to == 0 ? celsius : to == 1 ? celsius * 1.8 + 32 : celsius + 273.15;
            // At the exact lower boundary floating-point round-off must not create negative Kelvin.
            if (value == minimum && to == 2) result = 0;
        }
        if (!Double.isFinite(result)) throw new IllegalArgumentException("Value is too large to convert.");
        return from == to ? value : result;
    }
}
