package com.dantelab.testvkapplication.chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dantelab.testvkapplication.R;
import com.dantelab.testvkapplication.chat.adapter.ChatMessageTextViewHolder;
import com.dantelab.testvkapplication.chat.adapter.ChatRecyclerAdapter;
import com.dantelab.testvkapplication.common.adapters.RecyclerViewScrollListener;
import com.dantelab.testvkapplication.common.utils.Utils;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ChatFragment extends Fragment implements RecyclerViewScrollListener.Listener {


    private static final String LOG_TAG = "ChatFragment";
    private static final String ARG_USER = "user";
    private static final int LOAD_COUNT = 25;

    // TODO: Rename and change types of parameters
    private VKApiUserFull mUser;
    private VKRequest currentUpdateRequest;
    private boolean endOfMessages = false;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.text_view_message) EditText mMessageEditText;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Vk user to chat.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(VKApiUser user) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(new ChatRecyclerAdapter(getContext()));


        int right_outcome = getResources().getDimensionPixelSize(R.dimen.chat_message_offset_outcome);
        int left_income = getResources().getDimensionPixelSize(R.dimen.chat_message_offset_income);
        int side_regular = getResources().getDimensionPixelSize(R.dimen.chat_message_offset_regular);

        int top  = getResources().getDimensionPixelSize(R.dimen.chat_message_offset_bottom);
        int bottom = getResources().getDimensionPixelSize(R.dimen.chat_message_offset_top);

        mRecyclerView.addItemDecoration(new ChatMessageTextViewHolder.MessageItemDecorator(
                new Rect(side_regular, top, right_outcome, bottom),
                new Rect(left_income, top, side_regular, bottom))
        );

        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(this, true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadMessages(LOAD_COUNT, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    /** Загрузка истории сообщений
     *
     */
    private void loadMessages(final Integer count, Integer offset){

        if (currentUpdateRequest != null) {
            return;
        }

        final boolean reload = offset == 0;

        if (!reload && endOfMessages) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);


        VKRequest vkRequest = new VKRequest(
                "messages.getHistory",
                VKParameters.from(VKApiConst.USER_ID, mUser.id, VKApiConst.COUNT, count, VKApiConst.OFFSET, offset),
                VKApiGetMessagesResponse.class
        );

        final WeakReference<ChatFragment> weakThis = Utils.weak(this);
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                ChatFragment strongThis = weakThis.get();
                if (strongThis != null) {
                    VKApiGetMessagesResponse messages = (VKApiGetMessagesResponse) response.parsedModel;
                    strongThis.endOfMessages = count > messages.count;
                    strongThis.applyMessageResponse((VKApiGetMessagesResponse) response.parsedModel, reload);
                    strongThis.mProgressBar.setVisibility(View.GONE);
                    strongThis.currentUpdateRequest = null;
                }

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(LOG_TAG, error.toString());
                ChatFragment strongThis = weakThis.get();
                if (strongThis != null) {
                    mProgressBar.setVisibility(View.GONE);
                    strongThis.currentUpdateRequest = null;
                }
            }
        });
    }


    private void updateToolbar(){
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            assert supportActionBar != null;

            if (mUser != null) {
                supportActionBar.setTitle(mUser.first_name + " " + mUser.last_name);
                supportActionBar.setSubtitle(mUser.online ? R.string.status_online : R.string.status_offline);
            }
        }
    }


    private  void applyMessageResponse(VKApiGetMessagesResponse response, Boolean reload) {
        ChatRecyclerAdapter adapter = (ChatRecyclerAdapter) mRecyclerView.getAdapter();
        if (adapter != null) {
            if (reload) adapter.setMessages(response.items);
            else adapter.appendMessages(response.items);
        }
    }

    private  void appendMessageToStart(VKApiMessage message, Boolean scrollToMessage) {
        ChatRecyclerAdapter adapter = (ChatRecyclerAdapter) mRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.appendMessagesToStart(Collections.singletonList(message));
        }

        if (scrollToMessage) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @OnClick(R.id.button_send_message)
    public void sendMessage(){

        final CharSequence text = mMessageEditText.getText();

        if (text.length() == 0) {
            return;
        }

        final WeakReference<ChatFragment> weakThis = Utils.weak(this);

        new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID, mUser.id, VKApiConst.MESSAGE, text ), null).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onError(VKError error) {
                super.onError(error);
            }

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                ChatFragment strongThis = weakThis.get();
                if (strongThis == null) {
                    return;
                }

                int messageId = response.json.optInt("response");
                VKApiMessage message = new VKApiMessage();
                message.id = messageId;
                message.body = text.toString();
                message.out = true;
                message.user_id = strongThis.mUser.getId();
                message.date = System.currentTimeMillis();
                strongThis.appendMessageToStart(message, true);

            }
        });
    }


    @Override
    public void onMaxScrollReached(RecyclerView recyclerView, int newState) {
        loadMessages(LOAD_COUNT, recyclerView.getAdapter().getItemCount());
    }
}
