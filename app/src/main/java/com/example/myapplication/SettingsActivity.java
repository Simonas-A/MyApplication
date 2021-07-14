package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.Checkbox;

public class SettingsActivity extends AppCompatActivity {

    int delay = 0;
    boolean skipOnClick = true;
    boolean darkMode = false;
    boolean animations = true;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        delay = getIntent().getIntExtra("Delay", 12);
        skipOnClick = getIntent().getBooleanExtra("skipOnClick", true);
        darkMode = getIntent().getBooleanExtra("darkMode",true);
        animations = getIntent().getBooleanExtra("animations", true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch swch = (Switch)findViewById(R.id.switch2);
        swch.setChecked(darkMode);

        Switch swch1 = (Switch)findViewById(R.id.switch3);
        swch1.setChecked(animations);

        SeekBar sk = (SeekBar)findViewById(R.id.seekBar);
        sk.setMax(50);
        //sk.setMin(0);
        sk.setProgress(delay);

        CheckBox ck = findViewById(R.id.checkBox);
        ck.setChecked(skipOnClick);

        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText("Delay: " + (float)delay / 10 + " seconds");

        if (darkMode) {

            getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));
            ck.setTextColor(Color.rgb(255,173,0));
            textView.setTextColor(Color.rgb(255,173,0));
            swch.setTextColor(Color.rgb(255,173,0));
            swch1.setTextColor(Color.rgb(255,173,0));

        }
        else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            ck.setTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            swch.setTextColor(Color.BLACK);
            swch1.setTextColor(Color.BLACK);
        }




        sk.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                        int min = 0;
                        if(progressValue < min) {
                            seekBar.setProgress(min);
                        }

                        delay = progressValue;
                        TextView textView = (TextView)findViewById(R.id.textView2);
                        textView.setText("Delay: " + (float)progressValue / 10 + " seconds");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent data = new Intent();
        data.putExtra("Delay", delay);
        data.putExtra("skipOnClick", skipOnClick);
        data.putExtra("darkMode",darkMode);
        data.putExtra("animations",animations);
        setResult(RESULT_OK, data);
        finish();
        return true;
    }

    public void checkBoxClicked(View v) {

        skipOnClick = ((CheckBox)v).isChecked();
    }

    public void returnClicked(View v) {
        Intent data = new Intent();
        data.putExtra("Delay", delay);
        data.putExtra("skipOnClick", skipOnClick);
        data.putExtra("darkMode",darkMode);
        data.putExtra("animations",animations);
        setResult(RESULT_OK, data);
        finish();
        //Toast t = Toast.makeText(getApplicationContext(), "OPAPA", Toast.LENGTH_LONG);
        //t.show();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("Delay", delay);
        data.putExtra("skipOnClick", skipOnClick);
        data.putExtra("darkMode",darkMode);
        data.putExtra("animations",animations);
        setResult(RESULT_OK, data);
        finish();
        //Toast t = To
    }

    public void toggleDarkMode(View v) {
        Switch swch = (Switch)findViewById(R.id.switch2);
        Switch swch1 = (Switch)findViewById(R.id.switch3);
        CheckBox ck = findViewById(R.id.checkBox);
        TextView textView = (TextView)findViewById(R.id.textView2);
        darkMode = swch.isChecked();

        if (darkMode) {

            getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));
            ck.setTextColor(Color.rgb(255,173,0));
            textView.setTextColor(Color.rgb(255,173,0));
            swch.setTextColor(Color.rgb(255,173,0));
            swch1.setTextColor(Color.rgb(255,173,0));

        }
        else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            ck.setTextColor(Color.BLACK);
            textView.setTextColor(Color.BLACK);
            swch.setTextColor(Color.BLACK);
            swch1.setTextColor(Color.BLACK);
        }
    }

    public void toggleAnimations(View v) {
        Switch swch1 = (Switch)findViewById(R.id.switch3);
        animations = swch1.isChecked();
    }
}