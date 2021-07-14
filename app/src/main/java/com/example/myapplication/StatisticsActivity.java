package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class StatisticsActivity extends AppCompatActivity {

    //ColorScale[] colorScale;
    ColorScale[] colorScale;
    Boolean darkMode;

    int screenWidth;
    int screenHeight;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setTitle("Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        size = screenWidth - 100;


        Intent intent = getIntent();
        colorScale = GetStatsFromString(intent.getStringExtra("Stats"));
        darkMode = intent.getBooleanExtra("darkMode", true);
        //ColorScale[] colorScale = (ColorScale[]) intent.getParcelableArrayExtra("Stats");

        Bitmap imageBitmap = GetChart(colorScale, false, true);
        TextView txtView = (TextView)findViewById(R.id.textView3);
        txtView.setText(GetText(colorScale));

        ImageView img = (ImageView) findViewById(R.id.imageView4);
        img.setImageBitmap(imageBitmap);

        Switch swch = (Switch)findViewById(R.id.switch1);
        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton3);

        Button button = (Button) findViewById(R.id.button3);
        Spannable buttonLabel = new SpannableString(" ");
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), android.R.drawable.ic_menu_info_details,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        button.setText(buttonLabel);

        //Button btn = (Button)findViewById(R.id.button5);
        //Button btn1 = (Button)findViewById(R.id.button3);
        //btn.getLayoutParams().width=(int)(screenWidth * 0.15);
        //btn1.getLayoutParams().width=(int)(screenWidth * 0.15);
        //btn.setWidth((int)(screenWidth * 0.057));
        //btn1.setWidth((int)(screenWidth * 0.057));

        if (darkMode) {

            getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));
            txtView.setTextColor(Color.rgb(255,173,0));
            swch.setTextColor(Color.rgb(255,173,0));
            rb1.setTextColor(Color.rgb(255,173,0));
            rb2.setTextColor(Color.rgb(255,173,0));

        }
        else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            txtView.setTextColor(Color.BLACK);
            swch.setTextColor(Color.BLACK);
            rb1.setTextColor(Color.BLACK);
            rb2.setTextColor(Color.BLACK);
        }

        /*
        String labelText = "";
        for (int i = 0; i < 12; i++)
        {
            double avg = colorScale[i].totalDistance;
            if (colorScale[i].totalHits > 0)
            {
                avg /= colorScale[i].totalHits;
            }
            labelText += "Hue (" + colorScale[i].hue + "): " + colorScale[i].totalHits + " - " + avg + ""+ '\n';
        }
        TextView mTextView = (TextView) findViewById(R.id.textView3);
        mTextView.setText(labelText);
        */
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private String GetText(ColorScale[] colorScale) {
        int totalDistance = 0;
        int totalTime = 0;
        int totalHits = 0;

        DecimalFormat df = new DecimalFormat("0.0");

        for (int i = 0; i < 12; i++)
        {
            totalDistance += colorScale[i].totalDistance;
            totalTime += colorScale[i].totalTimeMs;
            totalHits += colorScale[i].totalHits;
        }

        String Text;
        if (totalHits > 0) {
            Text = "Average score: " + df.format((double)totalDistance / totalHits) + "" + '\n';
            df = new DecimalFormat("0.00");
            Text += "Average time: " + df.format(((double)totalTime / totalHits) / 1000) + " s" + '\n';
            Text += "Total clicks: " + totalHits + "";
        }
        else {
            Text = "No data";
        }

        return Text;
    }


    private Bitmap GetChart(ColorScale[] colorScale, Boolean showHitCount, Boolean accuracy) {
        Bitmap bmp = Bitmap.createBitmap(size, size + size / 10, Bitmap.Config.ARGB_8888);

        int maxBarHeight = 0;
        int maxLineHeight = 1;

        for (int i = 0; i < 12; i++) {
            if (colorScale[i].totalHits > 0) {
                if (accuracy) {
                    if (colorScale[i].totalDistance / colorScale[i].totalHits > maxBarHeight) {
                        maxBarHeight = colorScale[i].totalDistance / colorScale[i].totalHits;
                    }
                } else {
                    if (colorScale[i].totalTimeMs / colorScale[i].totalHits > maxBarHeight) {
                        maxBarHeight = colorScale[i].totalTimeMs / colorScale[i].totalHits;
                    }
                }

                if (colorScale[i].totalHits > maxLineHeight) {
                    maxLineHeight = colorScale[i].totalHits;
                }
            }
        }

        float[] points = new float[44];

        points[0] = (int)(0.15 * size);
        points[1] = (int)(0.9 * size) - (int) (0.45 * size) * colorScale[0].totalHits / maxLineHeight;
        int index = 2;

        Canvas c = new Canvas(bmp);
        Paint blackPaint = new Paint();

        blackPaint.setColor(darkMode ? Color.rgb(255,173,0) : Color.BLACK);
        blackPaint.setTextSize((int)(0.028 * size));
        blackPaint.setFakeBoldText(true);
        blackPaint.setStrokeWidth(10);
        //blackPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        Paint rulerPaint = new Paint();
        rulerPaint.setColor(Color.LTGRAY);
        rulerPaint.setStrokeWidth(10);

        c.drawLine((int)(0.05 * size), (int)(0.1 * size), (int)(0.05 * size), size, rulerPaint);

        for (int i = 0; i <= 9; i++) {

            c.drawText((i * maxBarHeight / 9) + "", (int)(0.052 * size), (10 - i) * (int)(0.1 * size) - (int)(0.015 * size), blackPaint);
            c.drawLine((int)(0.025 * size), (10 - i) * (int)(0.1 * size), (int)(0.075 * size), (10 - i) * (int)(0.1 * size), rulerPaint);
        }


        for (int i = 0; i < 12; i++) {

            if (i > 0) {
                points[index] = (int)(0.075 * size) * i + (int)(0.15 * size);
                points[index + 1] = (int)(0.9 * size) - (int)(0.45 * size) * colorScale[i].totalHits / maxLineHeight;
                if (i < 11) {
                    points[index + 2] = (int)(0.075 * size) * i + (int)(0.05 * size) + (int)(0.1 * size);
                    points[index + 3] = (int)(0.9 * size) - (int)(0.45 * size) * colorScale[i].totalHits / maxLineHeight;
                }
                index += 4;
            }


            if (colorScale[i].totalHits > 0) {
                Paint rectPaint = new Paint();
                rectPaint.setColor(Color.HSVToColor(new float[]{i * 30, 1, 1}));
                //rectPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

                //textPaint.setColor(InvertColor(Color.HSVToColor(new float[] {i*30, 1, 1})));
                int height;

                if (accuracy) {
                    height = (int)(0.9 * size) * (colorScale[i].totalDistance / colorScale[i].totalHits) / maxBarHeight;
                    c.drawText((colorScale[i].totalDistance / colorScale[i].totalHits) + "", i * (int)(0.075 * size) + (int)(0.124 * size), (int)(0.96 * size) - height, blackPaint);
                } else {
                    height = (int)(0.9 * size) * (colorScale[i].totalTimeMs / colorScale[i].totalHits) / maxBarHeight;
                    c.drawText((colorScale[i].totalTimeMs / colorScale[i].totalHits) + "", i * (int)(0.075 * size) + (int)(0.124 * size), (int)(0.96 * size) - height, blackPaint);
                }

                c.drawRect(i * (int)(0.075 * size) + (int)(0.125 * size), size - height, (int)(0.075 * size) * (i + 1) + (int)(0.1 * size), size, rectPaint);

            } else {
                c.drawText("No", i * (int)(0.075 * size) + (int)(0.124 * size), (int)(0.96 * size), blackPaint);
                c.drawText("data", i * (int)(0.075 * size) + (int)(0.124 * size), (int)(0.99 * size), blackPaint);
            }
        }


        if (showHitCount) {

            c.drawLines(points, blackPaint);
            for (int i = 0; i < 43; i += 2) {
                Paint p = new Paint();
                p.setColor(Color.HSVToColor(new float[]{(i / 4) * 30, 1, 1}));

                c.drawCircle(points[i], points[i + 1], (int)(0.015 * size), blackPaint);

                c.drawCircle(points[i], points[i + 1], (int)(0.008 * size), p);
            }

            for (int i = 0; i < 12; i++) {
                c.drawText(colorScale[i].totalHits + "", i * (int)(0.075 * size) + (int)(0.124 * size), (int)(size + size * 0.05),blackPaint);
            }

            c.drawText("Click count for hue", (int)(size / 2 - size * 0.05), (int)(size + size * 0.1), blackPaint);
        }

        //c.drawLine(0,0,150,700,p);

        /*
        for (int i = 0; i < 12; i++)
        {
            if (i > 0) {
                points[index ] = 75 * i + 50;
                points[index + 1] = 900 - 450 * colorScale[i].totalHits / maxLineHeight;
                if (i < 11) {
                    points[index + 2] = 75 * i + 50;
                    points[index + 3] = 900 - 450 * colorScale[i].totalHits / maxLineHeight;
                }
                index += 4;
            }

            if (colorScale[i].totalHits > 0) {
                //int color = Color.HSBtoRGB((float)i / 12, 1f, 1f);
                //int color = Color.HSBtoRGB(0.5f, 0.5f, 0.5f);
                int color = Color.HSVToColor(new float[] {i*30, 1, 1});
                int height = 900 * (colorScale[i].totalDistance / colorScale[i].totalHits) / maxBarHeight;

                for (int j = i * 75 + 25; j < (i + 1) * 75; j++) {
                    for (int h = 899; h > 900 - height; h--) {
                        bmp.setPixel(j, h, color);
                    }
                }
            }
        }



        Canvas c = new Canvas(bmp);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStrokeWidth(10);
        c.drawLines(points,p);
        for (int i = 0;i < 43; i+=2)
        {
            c.drawCircle(points[i], points[i+1],15,p);
        }
        c.drawRect(12,13,235,493,p);
        for (int i = 0;i < 12; i++)
        {
            //c.drawText();
        }
        //c.drawLine(0,0,150,700,p);
         */


        return bmp;

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

    public int InvertColor(int color) {
        int r = 255 - Color.red(color);
        int g = 255 - Color.red(color);
        int b = 255 - Color.red(color);

        return Color.rgb(r, g, b);
    }

    public void returnClicked(View v) {
        finish();
    }

    public void DeleteClicked(View v) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
        builder1.setMessage("Are you sure?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        setResult(RESULT_OK);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void switchClicked(View v) {
        Bitmap imageBitmap = GetChart(colorScale, ((Switch) v).isChecked(), ((RadioButton)findViewById(R.id.radioButton2)).isChecked());

        ImageView img = (ImageView) findViewById(R.id.imageView4);
        img.setImageBitmap(imageBitmap);
    }

    public void modeClicked(View v) {
        Bitmap imageBitmap = GetChart(colorScale, ((Switch)findViewById(R.id.switch1)).isChecked(), ((RadioButton)findViewById(R.id.radioButton2)).isChecked());

        ImageView img = (ImageView) findViewById(R.id.imageView4);
        img.setImageBitmap(imageBitmap);
    }

    public void infoClicked(View v) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());

        String msgText = "";

        if (((RadioButton)findViewById(R.id.radioButton2)).isChecked()) {
            msgText = "Bar chart shows average click distance for specific hue. Lower value means more accurate color recognition.";
        }
        else {
            msgText = "Bar chart shows average searching time for specific hue. Lower value means faster color recognition.";
        }

        builder1.setMessage(msgText);

        builder1.setNeutralButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (((Switch)findViewById(R.id.switch1)).isChecked()) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                            builder1.setMessage("Line chart shows total number of clicks for specific hue.");

                            builder1.setNeutralButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}