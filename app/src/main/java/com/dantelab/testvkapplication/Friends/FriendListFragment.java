package com.dantelab.testvkapplication.friends;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dantelab.testvkapplication.R;
import com.dantelab.testvkapplication.chat.ChatFragment;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKUsersArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import static com.dantelab.testvkapplication.common.utils.Utils.weak;




public class FriendListFragment extends Fragment implements FriendsRecyclerViewAdapter.OnItemClickListener {

    private static final String LOG_TAG = "FriendList";
    private static final int MULTIPLY_FACTOR = 1;

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private int mOnlineUsers;

    public static FriendListFragment newInstance() {
        Bundle args = new Bundle();
        FriendListFragment fragment = new FriendListFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        FriendsRecyclerViewAdapter viewAdapter = new FriendsRecyclerViewAdapter(getActivity());
        viewAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(viewAdapter);
        requestFriends();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    private void requestFriends(){
        final WeakReference<FriendListFragment> weakThis = weak(this);
        VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_50,photo_100,photo_200_orig,online")).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.e(LOG_TAG, error.toString());
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                FriendListFragment strongThis = weakThis.get();

                if (strongThis == null) {
                    return;
                }

                if (response.parsedModel instanceof VKUsersArray) {

                    VKUsersArray list = (VKUsersArray) response.parsedModel;

                    List<VKApiUserFull> friends = new ArrayList<>();
                    int online = 0;

                    int count = list.getCount();

                    for (int i = 0; i < count * MULTIPLY_FACTOR; i++) {
                        VKApiUserFull user = list.get(i);
                        friends.add(user);
                        if (list.get(i).online) {
                            online++;
                        }
                    }

                    strongThis.setFriends(friends, online);
                }
            }
        });
    }

    private void setFriends(List<VKApiUserFull> friends, Integer onlineCount) {
        ((FriendsRecyclerViewAdapter) recyclerView.getAdapter()).setFriends(friends);
        mOnlineUsers = onlineCount;
        updateToolbar();
    }


    private void updateToolbar(){
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            assert supportActionBar != null;

            supportActionBar.setTitle(R.string.title_activity_friend_list);
            supportActionBar.setSubtitle(String.format("online: %d", mOnlineUsers));
        }
    }


    @Override
    public void onAdapterItemClick(VKApiUserFull user, Integer position) {
        Log.d(LOG_TAG, user.toString());
        ChatFragment chatFragment = ChatFragment.newInstance(user);
         getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, chatFragment)
                .addToBackStack(null)
                .commit();
    }
}
