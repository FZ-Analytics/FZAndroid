package com.fz.fzapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fz.fzapp.R;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Heru Permana on 12/19/2017.
 */

public class AddJobView_adapter extends RecyclerView.Adapter<AddJobView_adapter.ViewHolder> {
    private List<String> mDataSet;
    private Context mContext;
    private Random mRandom = new Random();

    @Override
    public AddJobView_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(AddJobView_adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTypeEstimationEntry)
        TextView tvTypeEstimationEntry;
        @BindView(R.id.tvBlockEstimationEntry)
        TextView tvBlockEstimationEntry;
        @BindView(R.id.tvKgEstimationEntry)
        TextView tvKgEstimationEntry;
        @BindView(R.id.deleteItemEstimationItem)
        ImageView ivDeleteItemEstimationItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.deleteItemEstimationItem})
        public void onViewClicked(View view) {

            switch (view.getId()) {
                case R.id.deleteItemEstimationItem:

            }
        }
    }
}