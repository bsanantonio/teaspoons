package com.goodhousestudios.teaspoons;

public class WeightConverter {

    final private double ounce = 1;
    final private double pound = 0.0625f;
    final private double gram = 28.35;
    final private double kilogram = 0.02835;

    private double factor;

    public WeightConverter(String unit) {

        if (unit.equals("Ounce")) {
            factor = ounce;
        }
        else if (unit.equals("Pound")) {
            factor = pound;
        }
        else if (unit.equals("Gram")) {
            factor = gram;
        }
        else if (unit.equals("Kilogram")) {
            factor = kilogram;
        }
    }

    public double fromOunces(double measurement) {
        return measurement * factor;
    }

    public double toOunces(double measurement) {
        return measurement / factor;
    }
}
