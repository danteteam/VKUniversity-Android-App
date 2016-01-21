package com.dantelab.testvkapplication.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dantelab.testvkapplication.MyApplication;
import com.dantelab.testvkapplication.MainActivity;
import com.dantelab.testvkapplication.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class AuthActivityFragment extends Fragment implements VKCallback<VKAccessToken> {

    private static final String LOG_TAG = "Authorization";

    private BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyApplication.ACTION_ACCESS_TOKEN_CHANGED.equals(intent.getAction())){
                goToMainActivity();
            }
        }
    };


    @Bind(R.id.my_login_button) Button button;


    public AuthActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(bc, new IntentFilter(MyApplication.ACTION_ACCESS_TOKEN_CHANGED));
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.my_login_button)
    public void tryLogin(View view){
        if (getActivity() != null) {
            VKSdk.login(this.getActivity(), "friends", "messages", "offline");
        }
    }

    @Override
    public void onResult(VKAccessToken res) {
        Log.d(LOG_TAG, "Success");
    }

    @Override
    public void onError(VKError error) {
        Log.e(LOG_TAG, error.toString());
    }

    void goToMainActivity(){

        if (getActivity() == null || !VKSdk.isLoggedIn()) {
            return;
        }

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
