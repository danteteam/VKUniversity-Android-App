package com.dantelab.testvkapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dantelab.testvkapplication.Auth.AuthActivity;
import com.vk.sdk.VKSdk;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int LAUNCH_INTERVAL = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, LAUNCH_INTERVAL);
    }


    void checkLogin() {
        gotoActivity(VKSdk.isLoggedIn() ? MainActivity.class : AuthActivity.class);
    }


    void gotoActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
