 package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;

public class MenuActivity extends AppCompatActivity {

    ColorScale[] ColorHues = new ColorScale[12];

    int delay = 12;
    boolean skipOnClick = true;
    boolean darkMode = true;
    //boolean firstLaunch = true;
    boolean animations = true;

    CountDownTimer timer;
    int increment = 0;

    int screenWidth;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);

        SharedPreferences statsPref = getSharedPreferences("statsPref", MODE_PRIVATE);

        delay = preferences.getInt("Delay", 12);
        skipOnClick = preferences.getBoolean("skipOnClick", true);
        darkMode = preferences.getBoolean("darkMode", true);
        //firstLaunch = preferences.getBoolean("firstLaunch", true);
        animations = preferences.getBoolean("animations", true);


        for (int i = 0; i < 12; i++) {
            ColorScale cs = new ColorScale(i);
            cs.hue = statsPref.getInt("H" + i, i);
            cs.totalHits = statsPref.getInt("H" + i + "h", 0);
            cs.totalDistance = statsPref.getInt("H" + i + "d", 0);
            cs.totalTimeMs = statsPref.getInt("H" + i + "t", 0);
            ColorHues[i] = cs;
        }

        if (darkMode) {

            getWindow().getDecorView().setBackgroundColor(Color.rgb(33, 33, 33));

        } else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView header = (TextView) findViewById(R.id.textView4);
        header.setText("Color Match");
        //header.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/juice itc.ttf"));

        header.setTextColor(darkMode ? Color.rgb(255,173,0) : Color.BLACK);

        ImageView img = (ImageView) findViewById(R.id.imageView5);
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        header.setTextSize((int)(0.055 * Math.min(screenWidth,screenHeight)));


        Paint linePaint = new Paint();
        linePaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));
        linePaint.setStrokeWidth((int)(0.035 * Math.min(screenWidth,screenHeight)));


        //String name = getColoredSpanned("Hiren", "#800000");
        //String surName = getColoredSpanned("Patel","#000080");
        //header.setText(Html.fromHtml(name+" "+surName));


        timer = new CountDownTimer(10000, 10) {

            public void onTick(long millisUntilFinished) {
                increment++;

                if (increment > 1000000) {
                    increment = 0;
                }

                String[] Letters = new String[]{"C", "o", "l", "o", "r", " ", "M", "a", "t", "c", "h"};
                String text = "";
                for (int i = 0; i < 11; i++) {
                    Letters[i] = getColoredSpanned(Letters[i], String.format("#%06X", (0xFFFFFF & Color.HSVToColor(new float[]{(increment + i * 3) % 360, 1, 1}))));
                    text += Letters[i];
                }


                Bitmap bmp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bmp);

                linePaint.setColor(Color.HSVToColor(new float[]{increment % 360, 1, 1}));

                //canvas.drawLine(increment % screenWidth, increment % screenWidth, increment % screenWidth + 50, increment % screenHeight + 50, linePaint);

                int length = 20;
                //int start = increment % 360;
                int edgeLength = 2 * screenWidth + 2 * screenHeight;

                int widthAngle = screenWidth * 360 / edgeLength;
                int heightAngle = screenHeight * 360 / edgeLength;

                int linesCount = 9;

                int compensation = 0;
                

                for (int i = 0; i < linesCount; i++) {
                    int start = ((increment % 360) + (360 / linesCount) * i) % 360;

                    /*
                    if (start < widthAngle) {
                        if (start + length < widthAngle) {
                            canvas.drawLine(start * edgeLength / 360, compensation, (start + length) * edgeLength / 360, compensation, linePaint);
                            //txt.setText(start * edgeLength / 360 + " " + " 0 " + (start + length) * edgeLength / 360 + " 0");
                        } else {
                            canvas.drawLine(start * edgeLength / 360, compensation, widthAngle * edgeLength / 360, compensation, linePaint);
                            canvas.drawLine(widthAngle * edgeLength / 360 + compensation, 0, widthAngle * edgeLength / 360 + compensation, (length - widthAngle + start) * edgeLength / 360, linePaint);
                            //txt.setText(start * edgeLength / 360 + " " + " 0 " + widthAngle * edgeLength / 360 + " 0");
                        }
                    } else if (start < widthAngle + heightAngle) {
                        if (start + length < widthAngle + heightAngle) {
                            canvas.drawLine(widthAngle * edgeLength / 360 + compensation, (start - widthAngle) * edgeLength / 360, widthAngle * edgeLength / 360 + compensation, (start - widthAngle + length) * edgeLength / 360, linePaint);
                            //txt.setText(widthAngle * edgeLength / 360 + " " + (start - widthAngle) * edgeLength / 360 + " " + widthAngle * edgeLength / 360 + " " + (start - widthAngle + length) * edgeLength / 360);
                        } else {
                            canvas.drawLine(widthAngle * edgeLength / 360 + compensation, (start - widthAngle) * edgeLength / 360, widthAngle * edgeLength / 360 + compensation, (heightAngle) * edgeLength / 360, linePaint);
                            canvas.drawLine(widthAngle * edgeLength / 360, (heightAngle) * edgeLength / 360 - compensation, (widthAngle - (start + length - widthAngle - heightAngle)) * edgeLength / 360, (heightAngle) * edgeLength / 360 - compensation, linePaint);
                            //txt.setText(widthAngle * edgeLength / 360 + " " + (start - widthAngle) * edgeLength / 360 + " " + widthAngle * edgeLength / 360 + " " + (heightAngle) * edgeLength / 360);
                        }
                    } else if (start < 2 * widthAngle + heightAngle) {
                        if (start + length < 2 * widthAngle + heightAngle) {
                            canvas.drawLine((widthAngle - (start - widthAngle - heightAngle)) * edgeLength / 360, heightAngle * edgeLength / 360 - compensation, (widthAngle - (start + length - widthAngle - heightAngle)) * edgeLength / 360, heightAngle * edgeLength / 360 - compensation, linePaint);
                            //txt.setText((widthAngle - (start - widthAngle - heightAngle)) * edgeLength / 360 + " " + heightAngle * edgeLength / 360 + " " + (widthAngle - (start + length - widthAngle - heightAngle)) * edgeLength / 360 + " " + heightAngle * edgeLength / 360);
                        } else {
                            canvas.drawLine((widthAngle - (start - widthAngle - heightAngle)) * edgeLength / 360, heightAngle * edgeLength / 360 - compensation, 0, heightAngle * edgeLength / 360 - compensation, linePaint);
                            canvas.drawLine(compensation, (heightAngle) * edgeLength / 360, compensation, (heightAngle - (start + length - widthAngle * 2 - heightAngle)) * edgeLength / 360, linePaint);
                        }
                    } else {
                        if (start + length < 2 * widthAngle + 2 * heightAngle) {
                            canvas.drawLine(compensation, (heightAngle - (start - widthAngle * 2 - heightAngle)) * edgeLength / 360, compensation, (heightAngle - (start + length - widthAngle * 2 - heightAngle)) * edgeLength / 360, linePaint);
                        } else {
                            canvas.drawLine(compensation, (heightAngle - (start - widthAngle * 2 - heightAngle)) * edgeLength / 360, compensation, 0, linePaint);
                            canvas.drawLine(0, compensation, ((start + length) - 360) * edgeLength / 360, compensation, linePaint);
                        }
                    }

                     */
                    if (start < widthAngle) {
                        if (start + length < widthAngle) {
                            canvas.drawLine(start * edgeLength / 360, 0, (start + length) * edgeLength / 360, 0, linePaint);
                        } else {
                            canvas.drawLine(start * edgeLength / 360, 0, widthAngle * edgeLength / 360, 0, linePaint);
                            canvas.drawLine(screenWidth - 1, 0, screenWidth - 1, (length - widthAngle + start) * edgeLength / 360, linePaint);
                        }
                    } else if (start < widthAngle + heightAngle) {
                        if (start + length < widthAngle + heightAngle) {
                            canvas.drawLine(screenWidth - 1 , (start - widthAngle) * edgeLength / 360, screenWidth - 1, (start - widthAngle + length) * edgeLength / 360, linePaint);
                        } else {
                            canvas.drawLine(screenWidth - 1 , (start - widthAngle) * edgeLength / 360, screenWidth - 1, screenHeight - 1, linePaint);
                            canvas.drawLine(screenWidth - 1, screenHeight - 1, (widthAngle - (start + length - widthAngle - heightAngle)) * edgeLength / 360, screenHeight - 1, linePaint);
                        }
                    } else if (start < 2 * widthAngle + heightAngle) {
                        if (start + length < 2 * widthAngle + heightAngle) {
                            canvas.drawLine((widthAngle - (start - widthAngle - heightAngle)) * edgeLength / 360, screenHeight - 1, (widthAngle - (start + length - widthAngle - heightAngle)) * edgeLength / 360, screenHeight - 1, linePaint);
                        } else {
                            canvas.drawLine((widthAngle - (start - widthAngle - heightAngle)) * edgeLength / 360, screenHeight - 1, 0, screenHeight - 1, linePaint);
                            canvas.drawLine(0, screenHeight - 1, 0, (heightAngle - (start + length - widthAngle * 2 - heightAngle)) * edgeLength / 360, linePaint);
                        }
                    } else {
                        if (start + length < 2 * widthAngle + 2 * heightAngle) {
                            canvas.drawLine(0, (heightAngle - (start - widthAngle * 2 - heightAngle)) * edgeLength / 360, 0, (heightAngle - (start + length - widthAngle * 2 - heightAngle)) * edgeLength / 360, linePaint);
                        } else {
                            canvas.drawLine(0, (heightAngle - (start - widthAngle * 2 - heightAngle)) * edgeLength / 360, 0, 0, linePaint);
                            canvas.drawLine(0, 0, ((start + length) - 360) * edgeLength / 360, 0, linePaint);
                        }
                    }
                }


                /*
                if (start < screenWidth * 360 / edgeLength) {

                    if (start < (screenWidth + length)* 360 / edgeLength) {
                        canvas.drawLine(start * edgeLength / 360, 0, screenWidth - 1, 0, linePaint);
                        canvas.drawLine(screenWidth - 1, 0, screenWidth - 1, (screenWidth + length)* 360 - screenHeight * length , linePaint);
                    }
                    else {
                        canvas.drawLine(start * edgeLength / 360, 0, start * edgeLength / 360 + length, 0, linePaint);
                    }
                }
                else if (start < (screenWidth + screenHeight) * 360 / edgeLength) {
                    if (start < ((screenWidth + screenHeight) + length)* 360 / edgeLength) {
                        canvas.drawLine(start * edgeLength / 360, 0, screenWidth - 1, 0, linePaint);
                        canvas.drawLine(screenWidth - 1, 0, screenWidth - 1, (screenWidth + length)* 360 - screenHeight * length , linePaint);
                    }
                    else {
                        canvas.drawLine(screenWidth - 1, (start - widthAngle) * 360, 0, (start - widthAngle) * 360 + length, linePaint);
                    }
                }

                 */


                img.setImageBitmap(bmp);



                //Button btn = (Button)findViewById(R.id.button6);
                //btn.setTextColor(Color.HSVToColor(new float[]{(increment) % 360, 1, 1}));
                //btn.setBackgroundColor(InvertColor(Color.HSVToColor(new float[]{(increment) % 360, 1, 1})));

                header.setText(Html.fromHtml(text));


                //getWindow().getDecorView().setBackgroundColor(Color.HSVToColor(new float[]{increment, 1, 1}));
                //header.setTextColor(Color.HSVToColor(new float[]{increment * 360, 1, 1}));
            }

            public void onFinish() {
                timer.start();
            }

        };

        if (animations) {
            timer.start();
        }


    }

    public int InvertColor(int color) {
        int r = 255 - Color.red(color);
        int g = 255 - Color.red(color);
        int b = 255 - Color.red(color);

        return Color.rgb(r,g,b);
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public void Play(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        String statString = GetStatsString(ColorHues);
        //intent.putExtra("Stats", statString);
        intent.putExtra("Delay", delay);
        intent.putExtra("skipOnClick", skipOnClick);
        intent.putExtra("darkMode", darkMode);
        //intent.putExtra("firstLaunch", firstLaunch);

        startActivityForResult(intent, 0);

        /*
        if (firstLaunch) {
            firstLaunch = false;
            SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("firstLaunch", firstLaunch);
            edit.commit();
        }

         */

        //intent.putExtra("Stats", ColorHues);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                ColorHues = GetStatsFromString(data.getStringExtra("Stats"));
                SaveStatistics();

            }
        }
        else if (requestCode == 1)
        {
            if (resultCode == RESULT_OK){
                delay = data.getIntExtra("Delay", 12);
                skipOnClick = data.getBooleanExtra("skipOnClick", true);
                darkMode = data.getBooleanExtra("darkMode", true);
                animations = data.getBooleanExtra("animations", true);

                SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();

                edit.putInt("Delay", delay);
                edit.putBoolean("skipOnClick", skipOnClick);
                edit.putBoolean("darkMode", darkMode);
                edit.putBoolean("animations", animations);
                edit.commit();

                if (!animations) {
                    timer.cancel();
                    TextView txt = (TextView)findViewById(R.id.textView4);
                    txt.setText("Color Match");
                    txt.setTextColor(darkMode ? Color.rgb(255,173,0) : Color.BLACK);

                    ImageView img = (ImageView)findViewById(R.id.imageView5);
                    img.setImageBitmap(Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888));


                }
                else {
                    timer.start();
                }


            }
        }
        else if (requestCode == 2)
        {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i < 12; i++) {
                    ColorScale cs = new ColorScale(i);
                    ColorHues[i] = cs;
                }

                SaveStatistics();
            }
        }

        if (darkMode) {

            getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));

        }
        else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    public void Settings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Delay", delay);
        intent.putExtra("skipOnClick", skipOnClick);
        intent.putExtra("darkMode", darkMode);
        intent.putExtra("animations", animations);
        startActivityForResult(intent, 1);
    }

    public void Statistics(View v) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("Stats", GetStatsString(ColorHues));
        intent.putExtra("darkMode", darkMode);
        startActivityForResult(intent, 2);
    }

    private String GetStatsString(ColorScale[] colorHues) {
        String statString = "";
        for (int i = 0; i < 12; i++) {
            statString += colorHues[i].hue + ";" + colorHues[i].totalHits + ";" + colorHues[i].totalDistance + ";" + colorHues[i].totalTimeMs + ":";
        }
        return  statString;
    }

    private ColorScale[] GetStatsFromString(String stats) {
        ColorScale[] colorScales = new ColorScale[12];
        String[] line = stats.split(":");
        for (int i = 0; i < 12; i++) {
            String[] values = line[i].split(";");
            ColorScale cs = new ColorScale(i);
            cs.hue = Integer.parseInt(values[0]);
            cs.totalHits = Integer.parseInt(values[1]);
            cs.totalDistance = Integer.parseInt(values[2]);
            cs.totalTimeMs = Integer.parseInt(values[3]);
            colorScales[i] = cs;
        }

        return colorScales;
    }

    private void SaveStatistics() {
        SharedPreferences preferences = getSharedPreferences("statsPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        for (int i = 0; i < 12; i++)
        {
            edit.putInt("H" + i, ColorHues[i].hue);
            edit.putInt("H" + i + "h", ColorHues[i].totalHits);
            edit.putInt("H" + i + "d", ColorHues[i].totalDistance);
            edit.putInt("H" + i + "t", ColorHues[i].totalTimeMs);
        }

        edit.commit();
    }

    public void Quit(View v) {
        finish();
        System.exit(0);
    }
}