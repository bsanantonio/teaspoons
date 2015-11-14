package com.goodhousestudios.teaspoons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewTimerActivity extends AppCompatActivity {

    public static final String INDEX = "INDEX";
    public static final String LABEL = "LABEL";
    public static final String TIME = "TIME";
    public static final String TIME_TEXT = "TIME_TEXT";

    private TextView labelText;
    private TextView timerText;
    private int time;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        labelText = (TextView) findViewById(R.id.timer_label_text);
        timerText = (TextView) findViewById(R.id.timer_text);

        checkIntent();

        final Button button1 = (Button) findViewById(R.id.timer_button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 1;
                }
                updateTimerText();
            }
        });
        final Button button2 = (Button) findViewById(R.id.timer_button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 2;
                }
                updateTimerText();
            }
        });
        final Button button3 = (Button) findViewById(R.id.timer_button_3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 3;
                }
                updateTimerText();
            }
        });
        final Button button4 = (Button) findViewById(R.id.timer_button_4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 4;
                }
                updateTimerText();
            }
        });
        final Button button5 = (Button) findViewById(R.id.timer_button_5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 5;
                }
                updateTimerText();
            }
        });
        final Button button6 = (Button) findViewById(R.id.timer_button_6);
        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 6;
                }
                updateTimerText();
            }
        });
        final Button button7 = (Button) findViewById(R.id.timer_button_7);
        button7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 7;
                }
                updateTimerText();
            }
        });
        final Button button8 = (Button) findViewById(R.id.timer_button_8);
        button8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 8;
                }
                updateTimerText();
            }
        });
        final Button button9 = (Button) findViewById(R.id.timer_button_9);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 9;
                }
                updateTimerText();
            }
        });
        final Button button0 = (Button) findViewById(R.id.timer_button_0);
        button0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (String.valueOf(time).length() < 5) {
                    time *= 10;
                    time += 0;
                }
                updateTimerText();
            }
        });
        final Button buttonSave = (Button) findViewById(R.id.timer_button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NewTimerActivity.this, TimersActivity.class);
                int seconds = time % 100;
                int minutes = (time - seconds) / 100 % 100;
                int hours = (time - seconds - minutes) / 10000 % 100;
                time = seconds + minutes * 60 + hours * 60 * 60;
                intent.putExtra(TIME, time);
                intent.putExtra(LABEL, labelText.getText().toString());
                intent.putExtra(TIME_TEXT, timerText.getText().toString());
                if (index > -1) {
                    intent.putExtra(INDEX, index);
                }
                startActivity(intent);
            }
        });
        final ImageButton buttonClear = (ImageButton) findViewById(R.id.timer_button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                time = 0;
                updateTimerText();
            }
        });
    }

    private void checkIntent() {
        Intent intent = getIntent();
        String label = intent.getStringExtra(NewTimerActivity.LABEL);
        if (label == null || label.isEmpty()) {
            label = "Label";
        }

        index = intent.getIntExtra(NewTimerActivity.INDEX, -1);
        if (index > -1) {

            if (!label.equals("Label")) {
                labelText.setText(label);
            }
            int tempTime = intent.getIntExtra(NewTimerActivity.TIME, -1);
            if (tempTime > -1) {
                time = tempTime;
                updateTimerText();
            }
        }
    }

    private void updateTimerText() {
        String formatted = String.format("%05d", time);
        timerText.setText(formatted.charAt(0) + "h " + formatted.charAt(1) + formatted.charAt(2) +
            "m " + formatted.charAt(3) + formatted.charAt(4) + "s");
    }
}