package com.dantelab.testvkapplication.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dantelab.testvkapplication.R;
import com.dantelab.testvkapplication.common.adapters.RecyclerViewAdapter;
import com.dantelab.testvkapplication.common.utils.Utils;
import com.vk.sdk.api.model.VKApiUserFull;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ivan on 10/12/15.
 */
public class FriendsRecyclerViewAdapter extends RecyclerViewAdapter<FriendListItem> implements FriendListItem.OnClickListener {

    private List<VKApiUserFull> friends;
    private WeakReference<OnItemClickListener> listener = new WeakReference<>(null);

    public FriendsRecyclerViewAdapter(Context context) {
        super(context);
    }

    public void setFriends(List<VKApiUserFull> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    @Override
    public FriendListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendListItem friendListItem = new FriendListItem(layoutInflater.inflate(R.layout.list_item_friend, parent, false));
        friendListItem.setOnClickListener(this);
        return  friendListItem;
    }

    @Override
    public void onBindViewHolder(FriendListItem holder, int position) {
        holder.bindFriend(friends.get(position), layoutInflater.getContext());
    }

    @Override
    public int getItemCount() {
        return friends != null ? friends.size() : 0;
    }

    @Override
    public void onClick(FriendListItem item) {
        OnItemClickListener onItemClickListener = listener.get();
        if (onItemClickListener != null) {
            int adapterPosition = item.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                VKApiUserFull user = friends.get(adapterPosition);
                onItemClickListener.onAdapterItemClick(user, adapterPosition);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = Utils.weak(listener);
    }

    public interface OnItemClickListener {
        void onAdapterItemClick(VKApiUserFull user, Integer position);
    }
}
