package com.dantelab.testvkapplication.Friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dantelab.testvkapplication.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiUserFull;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ivan on 10/12/15.
 */
public class FriendListItem extends RecyclerView.ViewHolder {

    @Bind(R.id.avatarImage)
    ImageView avatarImage;
    @Bind(R.id.nameTextView)
    TextView nameTextView;
    @Bind(R.id.statusTextView)
    TextView statusTextView;

    private StringBuilder mStringBuilder = new StringBuilder();

    public FriendListItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindFriend(VKApiUserFull friend, Context context) {
        mStringBuilder.setLength(0);
        mStringBuilder.append(friend.first_name);
        mStringBuilder.append(" ");
        mStringBuilder.append(friend.last_name);
        nameTextView.setText(mStringBuilder);
        statusTextView.setText(friend.online ? context.getString(R.string.status_online) : context.getString(R.string.status_offline));
        Picasso.with(context).load(friend.photo_100).into(avatarImage);
    }
}
