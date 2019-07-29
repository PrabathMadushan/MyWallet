package com.prabath.mywallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.prabath.mywallet.BroadcastReceivers.ConnectivityReceiver;
import com.prabath.mywallet.Others.Init;

public class SplashActivity extends AppCompatActivity {

    float dX, dY;
    float sX,sY;

    ConnectivityReceiver connectivityReceiver;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
//        particleView=findViewById(R.id.particleView);
        final ImageView logo = findViewById(R.id.logo);



        Init.getInstance(this).setup();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
        }, 5000);


        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX )
                                .y(event.getRawY() + dY )
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.animate()
                                .x(sX)
                                .y(sY)
                                .setDuration(1000)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }

        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[] l=new int[2];
        ImageView logo = findViewById(R.id.logo);
        logo.getLocationOnScreen(l);

        sX=l[0];
        sY = l[1];
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }

}
