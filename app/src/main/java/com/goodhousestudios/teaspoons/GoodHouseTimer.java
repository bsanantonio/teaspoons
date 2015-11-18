package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

public class GoodHouseTimer {

    TimersActivity context;

    Boolean alarmPlayed;
    Uri notification;
    Ringtone ringtone;

    Boolean isRunning;
    String label;
    long readableTime;
    long startTime;
    long time;
    long lastTick;

    String timerText;

    private Handler handler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            update();
        }
    };

    public GoodHouseTimer(TimersActivity context, String label, long readableTime, long time) {
        this.context = context;

        alarmPlayed = false;

        isRunning = false;
        this.label = label;
        this.readableTime = readableTime;
        this.startTime = time;
        this.time = time * 1000;
        lastTick = 0;
        timerText = null;

        try {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this.context.getApplicationContext(), notification);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        startUpdateTimer();
    }

    public void update() {
        if (isRunning) {
            long difference = SystemClock.elapsedRealtime();
            time -= difference - lastTick;
            lastTick = difference;
            if (time < 0 && !alarmPlayed) {
                try {
                    WakeLocker.acquire(context);
                    ringtone.play();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                alarmPlayed = true;
            }
        }
        int seconds = (int) Math.abs(time / 1000) % 60;
        int minutes = (int) Math.abs((time / (1000 * 60)) % 60);
        int hours = (int) Math.abs((time / (1000 * 60 * 60)) % 24);
        if (time > 0) {
            timerText = hours + "h " + minutes + "m " + seconds + "s";
        } else {
            timerText = "-" + hours + "h " + minutes + "m " + seconds + "s";
        }

    }

    private void startUpdateTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    public void pause() {
        isRunning = false;
        if (alarmPlayed) {
            try {
                ringtone.stop();
                WakeLocker.release();
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