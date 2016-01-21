package com.dantelab.testvkapplication.chat.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dantelab.testvkapplication.R;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ivanbrazhnikov on 20.01.16.
 */
public class ChatMessageImageViewHolder extends ChatMessageViewHolder {

    public static ChatMessageImageViewHolder newInstance(LayoutInflater inflater, ViewGroup parent) {
        return new ChatMessageImageViewHolder(inflater.inflate(R.layout.list_item_chat_message_photo, parent, false));
    }

        @Bind(R.id.attachment_image)
        ImageView attachmentImageView;

        public ChatMessageImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    @Override
    protected View viewForLayout() {
        return attachmentImageView;
    }

    public void loadPhoto(VKApiPhoto photo) {
        attachmentImageView.setImageDrawable(null);
        Picasso.with(attachmentImageView.getContext()).load(Uri.parse(photo.photo_130)).into(attachmentImageView);
    }


}
