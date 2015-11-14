package com.goodhousestudios.teaspoons;

public class GoodHouseTimer {

    Boolean isRunning;
    String label;
    long startTime;
    long time;
    long lastTick;

    public GoodHouseTimer(String label, long time) {

        isRunning = false;
        this.label = label;
        this.startTime = time;
        this.time = time * 1000;
        lastTick = 0;
    }

    public void reset() {
        isRunning = false;
        time = startTime * 1000;
        lastTick = 0;
    }
}