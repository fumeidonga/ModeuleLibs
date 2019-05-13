package com.rebeau.technology.java.testdesignmodel.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rebeau.technology.R;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolderAdaptee> {


    @NonNull
    @Override
    public MyViewHolderAdaptee onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view, viewGroup, false);
        return new MyViewHolderAdaptee(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderAdaptee viewHolder, int i) {
        viewHolder.mTextView.setText("" + i);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
