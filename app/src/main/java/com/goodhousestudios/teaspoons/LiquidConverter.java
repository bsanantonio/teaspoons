package com.goodhousestudios.teaspoons;

public class LiquidConverter {

    final private double teaspoon = 0.1667;
    final private double tablespoon = 0.5;
    final private double cup = 8;
    final private double pint = 16;
    final private double quart = 32;
    final private double gallon = 128;

    private double factor;

    public LiquidConverter(String unit) {

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

    public double toFluidOunces(double measurement) {
        return measurement * factor;
    }
}
