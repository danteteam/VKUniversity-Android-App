package com.dantelab.testvkapplication.Friends;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dantelab.testvkapplication.R;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.List;

/**
 * Created by ivan on 10/12/15.
 */
public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendListItem> {

    private LayoutInflater inflater;
    private List<VKApiUserFull> friends;

    public void setFriends(List<VKApiUserFull> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public FriendsRecyclerViewAdapter(Activity context) {
        inflater = context.getLayoutInflater();
    }

    @Override
    public FriendListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendListItem(inflater.inflate(R.layout.list_item_friend_fl, parent, false));
    }

    @Override
    public void onBindViewHolder(FriendListItem holder, int position) {
        holder.bindFriend(friends.get(position), inflater.getContext());
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }
}
