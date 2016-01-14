package com.dantelab.testvkapplication.Friends;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dantelab.testvkapplication.Friends.FriendsRecyclerViewAdapter;
import com.dantelab.testvkapplication.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendListFragment extends Fragment {

    private static final String LOG_TAG = "FriendList";
    private static final int MULTIPLY_FACTOR = 10;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;


    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FriendsRecyclerViewAdapter(getActivity()));
        requestFriends();
    }

    private void requestFriends(){
        VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50,photo_100,photo_200_orig,online")).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(LOG_TAG, error.toString());
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ArrayList<VKApiUserFull> friends;
                if (response.parsedModel instanceof VKUsersArray) {

                    VKUsersArray list = (VKUsersArray) response.parsedModel;
                    friends = new ArrayList<>();
                    int count = list.getCount();

                    for (int i = 0; i < count*MULTIPLY_FACTOR; i++){
                         friends.add(list.get(i%count));
                    }

                } else {
                    friends = null;
                }

                if (friends != null && recyclerView.getAdapter() != null) {
                    ((FriendsRecyclerViewAdapter)recyclerView.getAdapter()).setFriends(friends);
                }
            }
        });
    }

}
