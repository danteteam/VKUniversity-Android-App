package com.dantelab.testvkapplication;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;


public class MyApplication extends Application {

    public static String ACTION_ACCESS_TOKEN_CHANGED = "ACTION_ACCESS_TOKEN_CHANGED";

    VKAccessTokenTracker tracker;
    @Override
    public void onCreate() {
        super.onCreate();

        tracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
                LocalBroadcastManager.getInstance(MyApplication.this).sendBroadcast(new Intent(ACTION_ACCESS_TOKEN_CHANGED));
            }
        };

        tracker.startTracking();

        VKSdk.initialize(this);
    }
}
