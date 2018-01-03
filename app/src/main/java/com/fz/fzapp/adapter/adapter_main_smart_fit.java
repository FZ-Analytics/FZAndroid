package com.fz.fzapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fz.fzapp.Smart_Fit.webView_Smart_Fit;
import com.fz.fzapp.R;
import com.fz.fzapp.model.DivisiRsp;
import com.fz.fzapp.model.MobileMenuRsp;
import com.fz.fzapp.utils.Smart_fit_Conditional;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Heru Permana on 12/11/2017.
 */

public class adapter_main_smart_fit extends RecyclerView.Adapter<adapter_main_smart_fit.ViewHolder> {
    private Context mContext;
    private List<MobileMenuRsp> mobileMenuPojoList;
    private List<DivisiRsp> divisiRsp;
    private Activity mActivity;
    String gsonToString;
    Smart_fit_Conditional smartFitConditional;

    public adapter_main_smart_fit(Activity activity, Context context, List<MobileMenuRsp> mobileMenuPojoList, String gsonToString) {
        this.mContext = context;
        this.mActivity = activity;
        this.mobileMenuPojoList = mobileMenuPojoList;
        this.gsonToString = gsonToString;

    }

    @Override
    public adapter_main_smart_fit.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lay_smatfit_item, parent, false);
        return new adapter_main_smart_fit.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(adapter_main_smart_fit.ViewHolder holder, int position) {
        holder.tv_title_smartfit_item.setText(mobileMenuPojoList.get(position).getTitle());
        holder.rlItemSmart.setTag(position);
        holder.rlMainSmartFitItem.setTag(mobileMenuPojoList.get(position).getTitle());
        holder.tv_title_smartfit_item.setTag(mobileMenuPojoList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mobileMenuPojoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rlItemSmart)
        RelativeLayout rlItemSmart;
        @BindView(R.id.rlMainSmartFitItem)
        RelativeLayout rlMainSmartFitItem;
        @BindView(R.id.tv_title_smartfit_item)
        TextView tv_title_smartfit_item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.rlItemSmart})
        public void onViewClicked(View view) {
            int intTag = (int) rlItemSmart.getTag();
            switch (view.getId()) {

                case R.id.rlItemSmart:
                    String webUrl = rlMainSmartFitItem.getTag().toString();
                    String id = tv_title_smartfit_item.getTag().toString();
                    smartFitConditional = new Smart_fit_Conditional(webUrl,id,mActivity,mContext,mobileMenuPojoList.get(getPosition()),gsonToString);
                    smartFitConditional.smatfitMenu();
                    break;
            }
        }

    }
}
