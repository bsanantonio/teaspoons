package com.goodhousestudios.teaspoons;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class TimersActivity extends AppCompatActivity {

    public static final String APP_TAG = "TimersActivity";

    private ListView lvItems;
    private GoodHouseApplication goodHouseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimersActivity.this, NewTimerActivity.class);
                startActivity(intent);
            }
        });
        goodHouseApplication = (GoodHouseApplication) getApplication();
        loadTimers();
        checkIntent();
        lvItems = (ListView) findViewById(R.id.list_timers);
        lvItems.setAdapter(new CountdownAdapter(TimersActivity.this, goodHouseApplication.goodHouseTimers));
    }

    private void checkIntent() {
        Intent intent = getIntent();

        String intentAction = intent.getStringExtra(NewTimerActivity.ACTION);
        if (intentAction != null && intentAction.equals(NewTimerActivity.SAVE)) {
            String label = intent.getStringExtra(NewTimerActivity.LABEL);
            if (label == null || label.isEmpty()) {
                label = "Label";
            }
            int readableTime = intent.getIntExtra(NewTimerActivity.READABLE_TIME, -1);
            int time = intent.getIntExtra(NewTimerActivity.TIME, -1);
            int index = intent.getIntExtra(NewTimerActivity.INDEX, -1);
            if (index > -1) {
                goodHouseApplication.goodHouseTimers.get(index).label = label;
                goodHouseApplication.goodHouseTimers.get(index).readableTime = readableTime;
                goodHouseApplication.goodHouseTimers.get(index).startTime = time;
                goodHouseApplication.goodHouseTimers.get(index).reset();
            } else {
                goodHouseApplication.goodHouseTimers.add(new GoodHouseTimer(this, label, readableTime, time));
            }
            saveTimers();
        }
        else if (intentAction != null && intentAction.equals(NewTimerActivity.DELETE)) {
            int index = intent.getIntExtra(NewTimerActivity.INDEX, -1);
            if (index > -1) {
                goodHouseApplication.goodHouseTimers.remove(index);
            }
            saveTimers();
        }
    }

    public void loadTimers() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        int labelCount = sharedPref.getInt("label_count", 0);
        if (goodHouseApplication.goodHouseTimers.size() == 0) {
            for (int i = 0; i < labelCount; i++) {
                String label = sharedPref.getString("timer_" + i + "_label", "Label");
                String readableTime = sharedPref.getString("timer_" + i + "_readable_time", "0");
                String time = sharedPref.getString("timer_" + i + "_time", "0");
                goodHouseApplication.goodHouseTimers.add(new GoodHouseTimer(this, label, Long.parseLong(readableTime), Long.parseLong(time)));
            }
        }
    }

    public void saveTimers() {
        int labelCount = goodHouseApplication.goodHouseTimers.size();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < labelCount; i++) {
            GoodHouseTimer timer = goodHouseApplication.goodHouseTimers.get(i);
            editor.putString("timer_" + i + "_label", timer.label);
            editor.putString("timer_" + i + "_readable_time", Long.toString(timer.readableTime));
            editor.putString("timer_" + i + "_time", Long.toString(timer.startTime));
        }
        editor.putInt("label_count", labelCount);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.about_dialog).setTitle(R.string.action_about);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (id == R.id.action_conversions) {
            Intent intent = new Intent(this, UnitConverterActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
