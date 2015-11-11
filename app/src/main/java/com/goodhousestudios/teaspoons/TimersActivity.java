package com.goodhousestudios.teaspoons;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class TimersActivity extends AppCompatActivity {

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
        checkIntent();
        lvItems = (ListView) findViewById(R.id.list_timers);
        lvItems.setAdapter(new CountdownAdapter(TimersActivity.this, goodHouseApplication.goodHouseTimers));
    }

    private void checkIntent() {
        Intent intent = getIntent();
        String label = intent.getStringExtra(NewTimerActivity.LABEL);
        int time = intent.getIntExtra(NewTimerActivity.TIME, -1);
        if (time != -1) {
            goodHouseApplication.goodHouseTimers.add(new GoodHouseTimer(label, time * 1000));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_conversions) {
            Intent intent = new Intent(this, UnitConverterActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
