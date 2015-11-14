package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
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
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_timer, parent, false);
            holder.btnPlay = (ImageButton) convertView.findViewById(R.id.timer_button_play);
            holder.btnLabel = (Button) convertView.findViewById(R.id.timer_label);
            holder.tvTime = (TextView) convertView.findViewById(R.id.timer_time);
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
        Button btnLabel;
        TextView tvTime;
        GoodHouseTimer goodHouseTimer;

        public void setData(GoodHouseTimer goodHouseTimer) {
            this.goodHouseTimer = goodHouseTimer;
            btnLabel.setText(this.goodHouseTimer.label);
            setupButton();
            updateTimeRemaining();
        }

        public void updateTimeRemaining() {
            if (goodHouseTimer.isRunning) {
                long difference = SystemClock.elapsedRealtime();
                goodHouseTimer.time -= difference - goodHouseTimer.lastTick;
                goodHouseTimer.lastTick = difference;
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

        public void pause() {
            goodHouseTimer.isRunning = false;
        }

        public void play() {
            goodHouseTimer.isRunning = true;
            goodHouseTimer.lastTick = SystemClock.elapsedRealtime();
        }

        private void setupButton() {
            btnPlay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (goodHouseTimer.isRunning) {
                        pause();
                        btnPlay.setImageResource(R.drawable.ic_play_circle_outline);
                    }
                    else {
                        play();
                        btnPlay.setImageResource(R.drawable.ic_pause_circle_outline);
                    }
                }
            });

            btnLabel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (goodHouseTimer.isRunning) {
                        pause();
                    }
                    GoodHouseApplication goodHouseApplication = (GoodHouseApplication) btnLabel.getContext().getApplicationContext();
                    int index = goodHouseApplication.goodHouseTimers.indexOf(goodHouseTimer);
                    Intent intent = new Intent(btnLabel.getContext(), NewTimerActivity.class);
                    int time = (int) goodHouseTimer.startTime;
                    intent.putExtra(NewTimerActivity.INDEX, index);
                    intent.putExtra(NewTimerActivity.TIME, time);
                    intent.putExtra(NewTimerActivity.LABEL, btnLabel.getText().toString());
                    btnLabel.getContext().startActivity(intent);
                }
            });
        }
    }
}
