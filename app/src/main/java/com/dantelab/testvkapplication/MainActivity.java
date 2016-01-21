package com.dantelab.testvkapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dantelab.testvkapplication.friends.FriendListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FriendListFragment fragment = FriendListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
    }


}
