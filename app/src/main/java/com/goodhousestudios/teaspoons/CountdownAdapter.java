package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            holder.button = (ImageButton) convertView.findViewById(R.id.timer_button_play);
            holder.tvLabel = (TextView) convertView.findViewById(R.id.timer_label);
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

        ImageButton button;
        TextView tvLabel;
        TextView tvTime;
        GoodHouseTimer goodHouseTimer;

        public void setData(GoodHouseTimer goodHouseTimer) {
            this.goodHouseTimer = goodHouseTimer;
            tvLabel.setText(this.goodHouseTimer.label);
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
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (goodHouseTimer.isRunning) {
                        pause();
                        button.setImageResource(R.drawable.ic_play_circle_outline);
                    }
                    else {
                        play();
                        button.setImageResource(R.drawable.ic_pause_circle_outline);
                    }
                }
            });
        }
    }
}
