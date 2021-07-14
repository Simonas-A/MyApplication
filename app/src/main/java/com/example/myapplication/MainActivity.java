package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.text.Layout;
import android.util.Size;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JButton;

public class MainActivity extends AppCompatActivity {

    //  "about" activity:
    //  rate game
    //  donate??
    //  game name :D

    long time0;
    long time1;

    boolean showHint = false;

    CountDownTimer updateTimer;


    ColorScale[] ColorHues = new ColorScale[12];

    int delay = 12;
    boolean skipOnClick = true;
    boolean darkMode = false;

    int freeByteValue = 0;
    int freeByte;

    int score = 0;
    int tries = 0;

    int targetColor = Color.RED;

    boolean first = true;
    boolean waiting = false;

    Bitmap bitmap;
    boolean imageDrawn = false;

    boolean firstLaunch = false;

    CountDownTimer timer;

    ConstraintLayout layout;

    int screenWidth;
    int screenHeight;

    int size; //square size

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        //getSupportActionBar().hide(); //hide the title bar

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        layout = findViewById(R.id.layout);
        //layout = getResources().getLayout(R.layout);
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;




        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            ConstraintLayout.LayoutParams paramsImg = (ConstraintLayout.LayoutParams) findViewById(R.id.imageView).getLayoutParams();
            paramsImg.topToTop = R.id.imageView2;


            size = (int)(screenHeight * 0.7);

            int imgSize = (int)(screenWidth * 0.8 - size) / 2;

            ImageView img1 = (ImageView)findViewById(R.id.imageView2);
            img1.getLayoutParams().height = imgSize;
            img1.getLayoutParams().width = imgSize;

            ImageView img2 = (ImageView)findViewById(R.id.imageView3);
            img2.getLayoutParams().height = imgSize;
            img2.getLayoutParams().width = imgSize;

            img1.requestLayout();
            img2.requestLayout();

            //int t = screenWidth;
            //screenWidth = screenHeight;
            //screenHeight = t;

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) findViewById(R.id.textView).getLayoutParams();
            params.horizontalBias = 0.92f; // here is one modification for example. modify anything else you want :)
            params.topToBottom = R.id.imageView2;
            findViewById(R.id.textView).setLayoutParams(params);
        }
        else {
            size = Math.min( (int)(screenHeight * 0.7), (int)(screenWidth * 0.9));

            ImageView img1 = (ImageView)findViewById(R.id.imageView2);
            img1.getLayoutParams().height = ((int)(screenHeight * 0.15));
            img1.getLayoutParams().width = ((int)(screenHeight * 0.15));

            ImageView img2 = (ImageView)findViewById(R.id.imageView3);
            img2.getLayoutParams().height = ((int)(screenHeight * 0.15));
            img2.getLayoutParams().width = ((int)(screenHeight * 0.15));

            img1.requestLayout();
            img2.requestLayout();
        }



        ((TextView)findViewById(R.id.textView)).setTextSize((float) (size * 0.02));



    }

    @Override
    public void onResume(){
        super.onResume();
        firstLaunch = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override protected void onStart() {

        delay = getIntent().getIntExtra("Delay", 12);
        skipOnClick = getIntent().getBooleanExtra("skipOnClick", true);
        //ColorHues = GetStatsFromString(getIntent().getStringExtra("Stats"));
        darkMode = getIntent().getBooleanExtra("darkMode", true);
        //firstLaunch = getIntent().getBooleanExtra("firstLaunch", false);

        SharedPreferences statsPref = getSharedPreferences("statsPref", MODE_PRIVATE);
        for (int i = 0; i < 12; i++) {
            ColorScale cs = new ColorScale(i);
            cs.hue = statsPref.getInt("H" + i, i);
            cs.totalHits = statsPref.getInt("H" + i + "h", 0);
            cs.totalDistance = statsPref.getInt("H" + i + "d", 0);
            cs.totalTimeMs = statsPref.getInt("H" + i + "t", 0);
            ColorHues[i] = cs;
        }

        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        firstLaunch = preferences.getBoolean("firstLaunch", true);

        /*
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);

        SharedPreferences statsPref = getSharedPreferences("statsPref", MODE_PRIVATE);

        delay = preferences.getInt("Delay", 12);
        skipOnClick = preferences.getBoolean("skipOnClick", true);

        for (int i = 0; i < 12; i++)
        {
            ColorScale cs = new ColorScale(i);
            cs.hue = statsPref.getInt("H" + i, i);
            cs.totalHits = statsPref.getInt("H" + i + "h", 0);
            cs.totalDistance = statsPref.getInt("H" + i + "d", 0);
            cs.totalTimeMs = statsPref.getInt("H" + i + "t", 0);
            ColorHues[i] = cs;
        }
         */


        super.onStart();


        TextView txt = (TextView)findViewById(R.id.textView);



        //txt.setText(darkMode ?  "Dark" : "Light");
        txt.setText("");


        CreateImage();
        ChangeImage();

        ImageView img = findViewById(R.id.imageView2);

        //bmp = Bitmap.createBitmap(new int[] { 1 }, 1, 1, Bitmap.Config.ARGB_8888);
        /*
        int frameWidth = 300;
        int frameHeight = 300;
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        frame.getContentPane().add(new DrawingComponent());
         */






        View view = findViewById(R.id.imageView);
        view.setOnTouchListener(new View.OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && !waiting) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    //TextView txtView = (TextView)findViewById(R.id.my_text_view);


                    if (x >= 0 && x < v.getWidth() && y >= 0 && y < v.getHeight()) {
                        ImageView img = findViewById(R.id.imageView2);
                        switch (freeByte) {
                            case 0:
                                //txtView.setText("X: " + event.getX() +"; Y: " + event.getY() + '\n' + "TARGET X: " + Color.green(targetColor) * 1000 / 255 + "; Y: " + Color.blue(targetColor) * 1000 / 255);
                                img.setBackgroundColor(Color.rgb(freeByteValue, x * 255 / size, y * 255 / size));
                                break;
                            case 1:
                                //txtView.setText("X: " + event.getX() +"; Y: " + event.getY() + '\n' + "TARGET X: " + Color.red(targetColor) * 1000 / 255 + "; Y: " + Color.blue(targetColor) * 1000 / 255);
                                img.setBackgroundColor(Color.rgb(x * 255 / size, freeByteValue, y * 255 / size));
                                break;
                            case 2:
                                //txtView.setText("X: " + event.getX() +"; Y: " + event.getY() + '\n' + "TARGET X: " + Color.red(targetColor) * 1000 / 255 + "; Y: " + Color.green(targetColor) * 1000 / 255);
                                img.setBackgroundColor(Color.rgb(x * 255 / size, y * 255 / size, freeByteValue));
                                break;
                        }
                        img.invalidate();
                    }

                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN && !waiting) {
                    time0 = System.currentTimeMillis();
                    DecimalFormat df = new DecimalFormat("0.0");

                    updateTimer = new CountDownTimer(10000, 100) {
                        TextView textUp = (TextView)findViewById(R.id.textView);
                        public void onTick(long millisUntilFinished) {
                            textUp.setText(df.format((System.currentTimeMillis() - time0) / 1000.0) + " s" );
                        }

                        public void onFinish() {
                            updateTimer.start();
                        }

                    }.start();

                }
                else if (event.getAction() == MotionEvent.ACTION_UP && !waiting) {
                    updateTimer.cancel();

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    int targetX = 0;
                    int targetY = 0;

                    int clickColor = 0;

                    if (x >= 0 && x < v.getWidth() && y >= 0 && y < v.getHeight()) {
                        time1 = System.currentTimeMillis();
                        //TextView txtView = (TextView) findViewById(R.id.my_text_view);
                        tries++;

                        int _score = 0;

                        switch (freeByte) {
                            case 0:
                                _score = (int) Math.sqrt(Math.pow((x * 255 / size - Color.green(targetColor)), 2) + Math.pow((y * 255 / size - Color.blue(targetColor)), 2));
                                //txtView.setText("GB Tikslas: " + Color.green(targetColor) + "; " + Color.blue(targetColor) + '\n' + "Pataikymas:" + x * 255 / 1000 + "; " + y * 255 / 1000);
                                clickColor = Color.rgb(Color.red(targetColor), x * 255 / size, y * 255 / size);
                                targetX = Color.green(targetColor);
                                targetY = Color.blue(targetColor);
                                break;
                            case 1:
                                _score = (int) Math.sqrt(Math.pow((x * 255 / size - Color.red(targetColor)), 2) + Math.pow((y * 255 / size - Color.blue(targetColor)), 2));
                                //txtView.setText("RB Tikslas: " + Color.red(targetColor) + "; " + Color.blue(targetColor) + '\n' + "Pataikymas:" + x * 255 / 1000 + "; " + y * 255 / 1000);
                                clickColor = Color.rgb(x * 255 / size, Color.green(targetColor), y * 255 / size);
                                targetX = Color.red(targetColor);
                                targetY = Color.blue(targetColor);
                                break;
                            case 2:
                                _score = (int) Math.sqrt(Math.pow((x * 255 / size - Color.red(targetColor)), 2) + Math.pow((y * 255 / size - Color.green(targetColor)), 2));
                                //txtView.setText("RG Tikslas: " + Color.red(targetColor) + "; " + Color.green(targetColor) + '\n' + "Pataikymas:" + x * 255 / 1000 + "; " + y * 255 / 1000);
                                clickColor = Color.rgb(x * 255 / size, y * 255 / size, Color.blue(targetColor));
                                targetX = Color.red(targetColor);
                                targetY = Color.green(targetColor);
                                break;
                        }
                        score += _score;

                        //txtView.setText("X: " + targetX + "; Y: " + targetY);

                        targetX = (targetX) * size / 255;
                        targetY = (targetY) * size / 255;



                        //TextView scoreTxt = (TextView) findViewById(R.id.textView);
                        //scoreTxt.setText("Score: " + _score + '\n' + "Tries: " + tries + '\n' + "AVG score: " + score / tries);

                        int invertTargetColor = InvertColor(targetColor);
                        int invertClickColor = InvertColor(clickColor);

                        ImageView imageView = findViewById(R.id.imageView);

                        Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                        //Long tsLong = System.currentTimeMillis();
                        //Target Point
                        for (int i = targetX - 15; i < targetX + 15; i+=1) {
                            if (i >= 0 && i < imageView.getWidth()) {
                                for (int j = targetY - 15; j < targetY + 15; j+=1) {
                                    if (j >= 0 && j < imageView.getHeight() && (j < targetY - 5 || j > targetY + 5 || i < targetX - 5 || i > targetX + 5)) {
                                        bmp.setPixel(i,j,invertTargetColor);
                                    }
                                }
                            }
                        }

                        //Click point
                        for (int i = x - 15; i < x + 15; i++) {
                            if (i >= 0 && i < imageView.getWidth() && (i < x - 5 || i > x + 5)) {
                                for (int j = y - 15; j < y + 15; j++) {
                                    if (j >= 0 && j < imageView.getHeight() && (j < y - 5 || j > y + 5)) {
                                        bmp.setPixel(i,j,invertClickColor);
                                    }
                                }
                            }
                        }


                        //bmp.setPixel(5,13,invertColor);
                        imageView.setImageBitmap(bmp);
                        //Long tsLong1 = System.currentTimeMillis();
                        //imageView.invalidate();

                        float[] values = new float[3];
                        Color.colorToHSV(targetColor, values);
                        ColorHues[(int)(values[0] / 30)].AddHit(_score,(int)(time1 - time0));

                        SaveStatistics();
                        //ColorScale cs = ColorHues[( (int)(values[0] / 30) )];
                        //cs.totalHits++;
                        //cs.AddHit(10,10);
                        //cs.AddHit(1,(int)(1234));


                        //txtView.setText((values[0] + "" + '\n' + "" + (time1-time0) + "" + '\n' + "" + (int)(values[0] / 30)));
                        //txtView.setText(ColorHues.size());
                        //Long tsLong = System.currentTimeMillis();
                        waiting = true;

                        CreateImage();

                        TextView txt = (TextView)findViewById(R.id.textView);
                        String Msg = "";

                        if (_score == 0) {
                            Msg = "Remarkably perfect!";
                        }
                        else if (_score < 5) {
                            Msg = "Excellent!";
                        }
                        else if (_score < 15) {
                            Msg = "Superb!";
                        }
                        else if (_score < 30) {
                            Msg = "Very well!";
                        }
                        else if (_score < 50) {
                            Msg = "Well done!";
                        }
                        else if (_score < 80) {
                            Msg = "Fine.";
                        }
                        else if (_score < 110) {
                            Msg = "Poorly...";
                        }
                        else if (_score < 180) {
                            Msg = "Awful";
                        }
                        else {
                            Msg = "Better luck next time...";
                        }

                        Msg += "\nDistance: " + _score;
                        Msg += "\nTime: " + (double)((time1 - time0)/1000.0 ) + " s";


                        txt.setText(Msg);




                        timer = new CountDownTimer(delay * 100, 1) {

                            public void onTick(long millisUntilFinished) {
                                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                            }

                            public void onFinish() {

                                if (waiting) {
                                    if (imageDrawn) {
                                        waiting = false;
                                        ChangeImage();
                                        txt.setText("");
                                    }
                                    //TextView mTextView = (TextView) findViewById(R.id.my_text_view);
                                    //mTextView.setText("Halo Text");
                                    //Long tsLong1 = System.currentTimeMillis();
                                    //mTextView.setText("" + (tsLong1 - tsLong));
                                }

                                //mTextField.setText("done!");
                            }
                        }.start();



                        /*
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (waiting) {

                                    waiting = false;
                                    Clicked(v1);
                                }
                            }
                        }, 1);   //5 seconds

                         */
                    }
                    else {
                        txt.setText("");
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP && waiting && skipOnClick)
                {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    if (x >= 0 && x < v.getWidth() && y >= 0 && y < v.getHeight()) {
                        //TextView mTextView = (TextView) findViewById(R.id.my_text_view);
                        //mTextView.setText("HOLLA Text" + imageDrawn);
                        if (imageDrawn) {
                            timer.cancel();
                            waiting = false;
                            txt.setText("");
                            //mTextView.invalidate();

                            ChangeImage();
                        }
                    }
                }
                return true;
            }
        });

        layout.setBackgroundColor(darkMode ? Color.rgb(33,33,33) : Color.WHITE);
        if (darkMode) {
            txt.setTextColor(Color.rgb(255,173,0));
        }
        else {
            txt.setTextColor(Color.BLACK);
        }

//        if (darkMode) {
//            layout.setBackgroundColor(Color.rgb(33,33,33));
//
//            getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));
//
//
//        }
//        else {
//            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
//        }

        // getWindow().getDecorView().setBackgroundColor(Color.rgb(33,33,33));

        if (firstLaunch) {
            firstLaunch = false;

            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("firstLaunch", firstLaunch);
            edit.commit();


            showHint = true;
            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
            builder1.setMessage("Thanks for downloading my game!");

            builder1.setNeutralButton(
                    "You're welcome!",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                            builder1.setMessage("Your target color will be displayed in the top left corner");

                            builder1.setNeutralButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                            builder1.setMessage("Drag your finger across colourful plane in the middle");

                                            builder1.setNeutralButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                                            builder1.setMessage("Color under your finger will be displayed in the top right corner");

                                                            builder1.setNeutralButton(
                                                                    "OK",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                                                            builder1.setMessage("Try to match this color with the target color");

                                                                            builder1.setNeutralButton(
                                                                                    "OK",
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                                                                            builder1.setMessage("When you are ready, release the finger");

                                                                                            builder1.setNeutralButton(
                                                                                                    "OK",
                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                                                                                                            builder1.setMessage("Your chosen color and actual target color will be displayed on the plane.\nHave fun!");

                                                                                                            builder1.setNeutralButton(
                                                                                                                    "OK",
                                                                                                                    new DialogInterface.OnClickListener() {
                                                                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                                                                        }
                                                                                                                    });

                                                                                                            AlertDialog alert11 = builder1.create();
                                                                                                            alert11.show();
                                                                                                        }
                                                                                                    });

                                                                                            AlertDialog alert11 = builder1.create();
                                                                                            alert11.show();
                                                                                        }
                                                                                    });

                                                                            AlertDialog alert11 = builder1.create();
                                                                            alert11.show();
                                                                        }
                                                                    });

                                                            AlertDialog alert11 = builder1.create();
                                                            alert11.show();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    });

            builder1.setNegativeButton(
                    "Skip tutorial",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent data = new Intent();
        data.putExtra("Stats", GetStatsString(ColorHues));
        setResult(RESULT_OK, data);
        finish();
        return true;
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

    public int InvertColor(int color) {
        int r = 255 - Color.red(color);
        int g = 255 - Color.red(color);
        int b = 255 - Color.red(color);

        return Color.rgb(r,g,b);
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("Stats", GetStatsString(ColorHues));
        setResult(RESULT_OK, data);
        finish();
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

    Random rand = new Random();
    int count = 0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CreateImage() {


        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[size * size];

        freeByteValue = rand.nextInt(256);
        freeByte = rand.nextInt(3);

        int value0 = rand.nextInt(256);
        int value1 = rand.nextInt(256);
        //int value0 = 15;
        //int value1 = 15;


        //int rnd = rand.nextInt(16777216);

        //int g = rand.nextInt(255);
        //int b = rand.nextInt(255);
        Long tsLong = System.currentTimeMillis();
        switch (freeByte) {
            case 0:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        //bitmap.setPixel(i, j, Color.rgb(freeByteValue, i * 255 / 1000, j * 255 / 1000));
                        pixels[j*size + i] = Color.rgb(freeByteValue, i * 255 / size, j * 255 / size);
                    }
                }
                targetColor = Color.rgb(freeByteValue, value0, value1);
                break;
            case 1:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        //bitmap.setPixel(i, j, Color.rgb(i * 255 / 1000, freeByteValue, j * 255 / 1000));
                        pixels[j*size + i] = Color.rgb(i * 255 / size,freeByteValue, j * 255 / size);
                    }
                }
                targetColor = Color.rgb(value0, freeByteValue, value1);
                break;
            case 2:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        //bitmap.setPixel(i, j, Color.rgb(i * 255 / 1000, j * 255 / 1000, freeByteValue));
                        pixels[j*size + i] = Color.rgb(i * 255 / size,j * 255 / size,freeByteValue);
                    }
                }
                targetColor = Color.rgb(value0, value1, freeByteValue);
                break;
        }



        bitmap.setPixels(pixels,0, size, 0, 0, size, size);
        imageDrawn = true;
        //TextView mTextView = (TextView) findViewById(R.id.my_text_view);

        //mTextView.setText(values[0] + "");
        Long tsLong1 = System.currentTimeMillis();
        //mTextView.setText("" + (tsLong1 - tsLong));
        //mTextView.setText("HOLLA Text");
    }

    public void StatisticsClicked(View v) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        String statString = GetStatsString(ColorHues);
        intent.putExtra("Stats", statString);
        //intent.putExtra("Stats", ColorHues);
        //intent.putExtra("Stats", ColorHues);
        startActivityForResult(intent,1);
        //startActivity(intent);
        //startActivityForResult(intent, 0);
        //Toast t = Toast.makeText(getApplicationContext(), "OPAPA", Toast.LENGTH_LONG);
        //t.show();
    }

    private String GetStatsString(ColorScale[] colorHues) {
        String statString = "";
        for (int i = 0; i < 12; i++) {
            statString += colorHues[i].hue + ";" + colorHues[i].totalHits + ";" + colorHues[i].totalDistance + ";" + colorHues[i].totalTimeMs + ":";
        }
        return  statString;
    }

    public void SettingsClicked(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Delay", delay);
        intent.putExtra("skipOnClick", skipOnClick);
        startActivityForResult(intent, 0);
        //Toast t = Toast.makeText(getApplicationContext(), "OPAPA", Toast.LENGTH_LONG);
        //t.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                delay = data.getIntExtra("Delay", 12);
                skipOnClick = data.getBooleanExtra("skipOnClick", true);

                SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();

                edit.putInt("Delay", delay);
                edit.putBoolean("skipOnClick", skipOnClick);
                edit.commit();

                //TextView mTextView = (TextView) findViewById(R.id.my_text_view);
                //mTextView.setText("" + delay);
            }
        }
        else if (requestCode == 1)
        {
            if (resultCode == RESULT_OK){

                for (int i = 0; i < 12; i++)
                {
                    ColorScale cs = new ColorScale(i);
                    ColorHues[i] = cs;
                }

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
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ChangeImage() {

        ImageView targetImg = findViewById(R.id.imageView3);
        targetImg.setBackgroundColor(targetColor);

        ImageView pointerImg = findViewById(R.id.imageView2);
        pointerImg.setBackgroundColor(darkMode ? Color.rgb(33,33,33) : Color.WHITE);

        ImageView img = findViewById(R.id.imageView);
        if (imageDrawn) {
            img.setImageBitmap(bitmap);
            imageDrawn = false;
        }

        if (showHint) {
            if (tries == 10) {
                showHint = false;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(findViewById(R.id.imageView).getContext());
                builder1.setMessage("You should check the statistics in the main menu. It will be more accurate with more clicks.");

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

    }

    public void textClicked(View v) {
        //TextView mTextView = (TextView) findViewById(R.id.my_text_view);
        //mTextView.setText("HOLLA Text");
    }
}