package com.dantelab.testvkapplication.chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.dantelab.testvkapplication.common.adapters.RecyclerViewAdapter;
import com.dantelab.testvkapplication.common.utils.Utils;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ivanbrazhnikov on 20.01.16.
 */



public class ChatRecyclerAdapter extends RecyclerViewAdapter<ChatMessageViewHolder> {

    private static final int VIEW_TYPE_TEXT = 0;
    private static final int VIEW_TYPE_PHOTO = 1;

    private List<VKApiMessage> messages;
    private SparseArray<VKApiPhoto> photoIndex;

    private ExecutorService updateExecutor;

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public ChatRecyclerAdapter(Context context) {
        super(context);
        messages = new ArrayList<>();
        photoIndex = new SparseArray<>();
        updateExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  viewType == VIEW_TYPE_TEXT
                ? ChatMessageTextViewHolder.newInstance(layoutInflater, parent)
                : ChatMessageImageViewHolder.newInstance(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        VKApiMessage message = messages.get(position);
        holder.bind(message);
        VKApiPhoto photo = photoIndex.get(message.getId());
        if (photo != null && holder instanceof ChatMessageImageViewHolder) {
            ((ChatMessageImageViewHolder) holder).loadPhoto(photo);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  photoIndex.get(messages.get(position).getId()) == null ? VIEW_TYPE_TEXT : VIEW_TYPE_PHOTO;
    }

    public void appendMessages(List<VKApiMessage> messages) {
        _importMessages(messages, true, false);
    }


    public void setMessages(List<VKApiMessage> messages) {
        _importMessages(messages, false, false);
    }

    public void appendMessagesToStart(List<VKApiMessage> messages) {
        _importMessages(messages, true, true);
    }

    private void _importMessages(final List<VKApiMessage> messages, final boolean append, final boolean toStart) {
        final WeakReference<ChatRecyclerAdapter> weak = Utils.weak(this);
        final WeakReference<Handler> weakHandler = Utils.weak(this.mainThreadHandler);
        updateExecutor.submit(new Runnable() {
            @Override
            public void run() {
                final SparseArray<VKApiPhoto> index = new SparseArray<VKApiPhoto>();

                for (VKApiMessage m : messages) {
                    VKApiPhoto photo = Utils.messageFirstPhoto(m);
                    if (photo != null) {
                        index.append(m.getId(), photo);
                    }
                }
                final Handler strongHandler = weakHandler.get();

                if (strongHandler != null) {
                    strongHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ChatRecyclerAdapter strongThis = weak.get();
                            if (strongThis != null) {
                                strongThis._setup(messages, index, append, toStart);
                            }
                        }
                    });
                }
            }
        });
    }

    private void _setup(List<VKApiMessage> newMessages, @Nullable SparseArray<VKApiPhoto> photoIndex, boolean append, boolean toStart) {
        int start;


        if (!append) {
            this.messages.clear();
            this.photoIndex.clear();
            start = 0;
        } else  {
            if (toStart) {
                start = 0;
            } else  {
                start = this.messages.size();
            }
        }


        if (photoIndex != null) {
            for (int i = 0; i<photoIndex.size(); i++) {
                this.photoIndex.append(photoIndex.keyAt(i), photoIndex.valueAt(i));
            }
        }

        this.messages.addAll(start, newMessages);
        this.notifyItemRangeInserted(start, newMessages.size());
    }
}
