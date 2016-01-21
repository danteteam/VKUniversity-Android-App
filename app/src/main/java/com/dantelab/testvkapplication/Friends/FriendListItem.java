package com.dantelab.testvkapplication.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dantelab.testvkapplication.R;
import com.dantelab.testvkapplication.common.utils.Utils;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiUserFull;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by ivan on 10/12/15.
 */
public class FriendListItem extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.avatarImage)
    ImageView avatarImage;
    @Bind(R.id.nameTextView)
    TextView nameTextView;
    @Bind(R.id.statusTextView)
    TextView statusTextView;

    private WeakReference<OnClickListener> listener;

    private StringBuilder mStringBuilder = new StringBuilder();

    public FriendListItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
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

    public void setOnClickListener(OnClickListener listener) {
        if (this.listener != null) {
            this.listener.clear();
        }
        this.listener = Utils.weak(listener);
    }

    @Override
    public void onClick(View v) {
        if (this.listener != null) {
            OnClickListener onClickListener = this.listener.get();
            if (onClickListener != null) {
                onClickListener.onClick(this);
            }
        }
    }


    public interface OnClickListener {
        void onClick(FriendListItem item);
    }
}
