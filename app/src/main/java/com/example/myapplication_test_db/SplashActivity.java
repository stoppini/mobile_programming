package com.example.myapplication_test_db;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long MIN_WAIT_INTERVAL = 1000L;
    private long mStartTime = -1L;
    private static final String TAG_LOG = SplashActivity.class.getName();
    // cosa cristo è START_TIME_KEY???
    private static final String START_TIME_KEY = "com.example.cardamarket.START_TIME_KEY";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(START_TIME_KEY, mStartTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(savedInstanceState != null) {
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);
        }

        final ImageView logoImageView = (ImageView) findViewById(R.id.splash_imageview);

        logoImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                long elapsedTime = SystemClock.uptimeMillis() - mStartTime;
                if(elapsedTime >= MIN_WAIT_INTERVAL)
                {
                    Log.d(TAG_LOG, "OK! Let's go ahead...");
                    goAhead();
                } else {
                    Log.d(TAG_LOG, "Too early!");
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mStartTime == -1L) {
            mStartTime = SystemClock.uptimeMillis();
        }
        Log.d(TAG_LOG, "Activity started");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG_LOG, "Activity destroyed");
    }

    private void goAhead() {
        final Intent intent = new Intent(this, FirstAccessActivity.class);
        startActivity(intent);
        finish();
    }
}