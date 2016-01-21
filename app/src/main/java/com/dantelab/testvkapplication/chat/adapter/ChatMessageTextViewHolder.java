package com.dantelab.testvkapplication.chat.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dantelab.testvkapplication.R;
import com.vk.sdk.api.model.VKApiMessage;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ivanbrazhnikov on 20.01.16.
 */
public class ChatMessageTextViewHolder extends ChatMessageViewHolder {

    public static ChatMessageTextViewHolder newInstance(LayoutInflater inflater, ViewGroup parent) {
        return new ChatMessageTextViewHolder(inflater.inflate(R.layout.list_item_chat_message_text, parent, false));
    }


    @Bind(R.id.text_message)
    TextView messageTextView;

    public ChatMessageTextViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(VKApiMessage message) {
        super.bind(message);
        messageTextView.setText(message.body);
    }

    @Override
    protected View viewForLayout() {
        return  messageTextView;
    }
}

