package com.mcgill.bandop;

public class Reward {

    private double value;

    public Reward() {

    }

    public boolean isValid() {
        return value >= 0 && value <= 1;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
