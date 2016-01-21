package com.dantelab.testvkapplication.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dantelab.testvkapplication.common.utils.Utils;

import java.lang.ref.WeakReference;

/**
 * Created by ivanbrazhnikov on 21.01.16.
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static String LOG_TAG = "RecyclerViewScrollListener";
    private WeakReference<Listener> listenerReference;
    private boolean reverse;

    public RecyclerViewScrollListener(Listener listener, boolean reverse){
        listenerReference = Utils.weak(listener);
        this.reverse = reverse;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        int currentScroll = recyclerView.computeVerticalScrollOffset();
        Log.d(LOG_TAG, String.format("%d", currentScroll));
        boolean reached = (reverse && currentScroll <= 0) ||  recyclerView.computeVerticalScrollRange() <= currentScroll;
        if (reached) {
            if (listenerReference.get() != null)
                listenerReference.get().onMaxScrollReached(recyclerView, newState);
        }
    }

    public interface Listener {
        void onMaxScrollReached(RecyclerView recyclerView, int newState);
    }
}
