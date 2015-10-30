package com.goodhousestudios.teaspoons;

public class DryConverter {

    final private double teaspoon = 0.02083333f;
    final private double tablespoon = 0.0625f;
    final private double cup = 1;
    final private double pint = 2;
    final private double quart = 4;
    final private double gallon = 16;

    private double factor;

    public DryConverter(String unit) {

        if (unit.equals("Teaspoon")) {
            factor = teaspoon;
        }
        else if (unit.equals("Tablespoon")) {
            factor = tablespoon;
        }
        else if (unit.equals("Cup")) {
            factor = cup;
        }
        else if (unit.equals("Pint")) {
            factor = pint;
        }
        else if (unit.equals("Quart")) {
            factor = quart;
        }
        else if (unit.equals("Gallon")) {
            factor = gallon;
        }
    }

    public double fromCups(double measurement) {
        return measurement / factor;
    }

    public double toCups(double measurement) {
        return measurement * factor;
    }

    public double toLiquidCups(double measurement) { return measurement * factor * 8; }
}
