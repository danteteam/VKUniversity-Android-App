package com.dantelab.testvkapplication.chat.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.dantelab.testvkapplication.R;
import com.vk.sdk.api.model.VKApiMessage;

/**
 * Created by ivanbrazhnikov on 21.01.16.
 */
public abstract class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    private boolean out = false;

    protected abstract View viewForLayout();

    public ChatMessageViewHolder(View itemView) {
        super(itemView);
    }


    public void bind(VKApiMessage message) {
        out = message.out;
        View viewForLayout = viewForLayout();
        if (viewForLayout != null) {

            RelativeLayout.LayoutParams textLayoutParams = (RelativeLayout.LayoutParams) viewForLayout.getLayoutParams();
            if (message.out) {
                viewForLayout.setBackgroundResource(R.drawable.message_background_outcome);
                textLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
                textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            } else {
                viewForLayout.setBackgroundResource(R.drawable.message_background_income);
                textLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
                textLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
        }
    }

    public boolean isOut() {
        return out;
    }


    public static class MessageItemDecorator extends RecyclerView.ItemDecoration {
        private Rect incomeRect;
        private Rect outcomeRect;

        public MessageItemDecorator(Rect incomeRect, Rect outcomeRect) {
            this.incomeRect = outcomeRect;
            this.outcomeRect = incomeRect;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (((ChatMessageViewHolder) parent.getChildViewHolder(view)).isOut()) {
                outRect.set(incomeRect);
            } else {
                outRect.set(outcomeRect);
            }
        }
    }
}
