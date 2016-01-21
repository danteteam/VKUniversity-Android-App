package com.dantelab.testvkapplication.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * Created by ivanbrazhnikov on 20.01.16.
 */
public abstract class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context){
        this.layoutInflater = LayoutInflater.from(context);
    }
}
