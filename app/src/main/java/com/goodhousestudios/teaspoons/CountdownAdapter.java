package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CountdownAdapter extends ArrayAdapter<GoodHouseTimer> {

    private LayoutInflater inflater;
    private List<ViewHolder> listHolders;
    private Handler handler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (listHolders) {
                for (ViewHolder holder : listHolders) {
                    holder.updateTimeRemaining();
                }
            }
        }
    };

    public CountdownAdapter(Context context, List<GoodHouseTimer> objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);
        listHolders = new ArrayList<>();
        startUpdateTimer();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_timer, parent, false);
            ImageButton btnPlay = (ImageButton) convertView.findViewById(R.id.timer_button_play);
            ImageButton btnReset = (ImageButton) convertView.findViewById(R.id.timer_button_reset);
            Button btnLabel = (Button) convertView.findViewById(R.id.timer_label);
            TextView tvTime = (TextView) convertView.findViewById(R.id.timer_time);
            holder = new ViewHolder(btnPlay, btnReset, btnLabel, tvTime);
            convertView.setTag(holder);
            synchronized (listHolders) {
                listHolders.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        return convertView;
    }

    private class ViewHolder {

        ImageButton btnPlay;
        ImageButton btnReset;
        Button btnLabel;
        TextView tvTime;
        GoodHouseTimer goodHouseTimer;

        public ViewHolder(ImageButton btnPlay, ImageButton btnReset, Button btnLabel, TextView tvTime) {

            this.btnPlay = btnPlay;
            this.btnReset = btnReset;
            this.btnLabel = btnLabel;
            this.tvTime = tvTime;
        }

        public void setData(GoodHouseTimer goodHouseTimer) {
            this.goodHouseTimer = goodHouseTimer;
            btnLabel.setText(this.goodHouseTimer.label);
            setupButton();
            updateTimeRemaining();
        }

        public void updateTimeRemaining() {
            if (goodHouseTimer.isRunning) {
                long difference = SystemClock.elapsedRealtime();
                goodHouseTimer.update(difference);
            }
            int seconds = (int) Math.abs(goodHouseTimer.time / 1000) % 60;
            int minutes = (int) Math.abs((goodHouseTimer.time / (1000 * 60)) % 60);
            int hours = (int) Math.abs((goodHouseTimer.time / (1000 * 60 * 60)) % 24);
            if (goodHouseTimer.time > 0) {
                tvTime.setText(hours + "h " + minutes + "m " + seconds + "s");
            } else {
                tvTime.setText("-" + hours + "h " + minutes + "m " + seconds + "s");
            }
        }

        private void setupButton() {
            btnPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (goodHouseTimer.isRunning) {
                        goodHouseTimer.pause();
                        btnPlay.setImageResource(R.drawable.ic_play_circle_outline);
                    }
                    else {
                        goodHouseTimer.play();
                        btnPlay.setImageResource(R.drawable.ic_pause_circle_outline);
                    }
                }
            });
            btnReset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnPlay.setImageResource(R.drawable.ic_play_circle_outline);
                    goodHouseTimer.reset();
                }
            });

            btnLabel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (goodHouseTimer.isRunning) {
                        goodHouseTimer.pause();
                    }
                    GoodHouseApplication goodHouseApplication = (GoodHouseApplication) btnLabel.getContext().getApplicationContext();
                    int index = goodHouseApplication.goodHouseTimers.indexOf(goodHouseTimer);
                    Intent intent = new Intent(btnLabel.getContext(), NewTimerActivity.class);
                    int readableTime = (int) goodHouseTimer.readableTime;
                    int time = (int) goodHouseTimer.startTime;
                    intent.putExtra(NewTimerActivity.INDEX, index);
                    intent.putExtra(NewTimerActivity.READABLE_TIME, readableTime);
                    intent.putExtra(NewTimerActivity.TIME, time);
                    intent.putExtra(NewTimerActivity.LABEL, btnLabel.getText().toString());
                    btnLabel.getContext().startActivity(intent);
                }
            });
        }
    }
}
