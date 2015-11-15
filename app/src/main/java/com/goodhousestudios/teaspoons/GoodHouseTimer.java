package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

public class GoodHouseTimer {

    Context context;

    Boolean alarmPlayed;
    Uri notification;
    Ringtone ringtone;

    Boolean isRunning;
    String label;
    long readableTime;
    long startTime;
    long time;
    long lastTick;

    public GoodHouseTimer(Context context, String label, long readableTime, long time) {
        this.context = context;

        alarmPlayed = false;

        isRunning = false;
        this.label = label;
        this.readableTime = readableTime;
        this.startTime = time;
        this.time = time * 1000;
        lastTick = 0;

        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this.context.getApplicationContext(), notification);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(long difference) {
        time -= difference - lastTick;
        lastTick = difference;
        if (time < 0 && !alarmPlayed) {
            try {
                ringtone.play();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            alarmPlayed = true;
        }
    }

    public void pause() {
        isRunning = false;
        if (alarmPlayed) {
            try {
                ringtone.stop();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        isRunning = true;
        lastTick = SystemClock.elapsedRealtime();
    }

    public void reset() {
        pause();
        alarmPlayed = false;

        time = startTime * 1000;
        lastTick = 0;
    }
}