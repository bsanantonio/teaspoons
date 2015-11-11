package com.goodhousestudios.teaspoons;

public class GoodHouseTimer {

    Boolean isRunning;
    String label;
    long time;
    long lastTick;

    public GoodHouseTimer(String label, long time) {

        isRunning = false;
        this.label = label;
        this.time = time;
        lastTick = 0;
    }
}