package com.fz.fzapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.R;
import com.fz.fzapp.common.Planning;
import com.fz.fzapp.common.SyncData;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;
import com.fz.fzapp.model.ReasonResponse;
import com.fz.fzapp.popup.ReasonStatus;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.SaveToSQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class ReasonList_adapter extends BaseAdapter {
    private static String TAG = "[adapter_TaskList]";
    private static Activity activity;
    private static Context mContext;
    static private List<ReasonResponse> reasonResponses;
    private static Integer intDuty;

    public ReasonList_adapter(Activity mActivity, Context mContext, List<ReasonResponse> reasonResponses, Integer intDuty) {
        this.mContext = mContext;
        this.reasonResponses = reasonResponses;
        this.activity = mActivity;
        this.intDuty = intDuty;
    }

    @Override
    public int getCount() {
        return reasonResponses.size();
    }

    @Override
    public Object getItem(int i) {
        return reasonResponses.get(i).getReasonid();
    }

    @Override
    public long getItemId(int i) {
        return reasonResponses.get(i).getReasonid();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = lInflater.inflate(R.layout.reasondetail_lay, null);
        }

        ViewHolder vhView = new ViewHolder(view);
        vhView.tvReasonName.setText(reasonResponses.get(i).getReasonname() + " / " + reasonResponses.get(i).getReasondesc());
        vhView.tvReasonName.setTag(i);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tvReasonName)
        TextView tvReasonName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.rlDetailReason, R.id.tvReasonName})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.rlDetailReason:
                case R.id.tvReasonName:
                    Integer idxView = (Integer) tvReasonName.getTag();
                    final String[] strReason = {""};
                    if (reasonResponses.get(idxView).getReasonname() == "Alasan Lain") {
                        ReasonStatus reasonStatus = new ReasonStatus(activity);
                        reasonStatus.show();
                        reasonStatus.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                strReason[0] = AllFunction.getStringFromSharedPref(mContext, Preference.prefOtherReason);
                            }
                        });
                    } else
                        strReason[0] = reasonResponses.get(idxView).getReasonname();

                    if (intDuty == 2) {
                        int countingArray = AllFunction.getIntFromSharedPref(mContext, "countingArray");
                        if (AllTaskList_adapter.getInstance().getAlltaskList().size() == countingArray + 1) {
                            uploadSync();
                            Intent SyncIntent = new Intent(mContext, SyncData.class);
                            mContext.startActivity(SyncIntent);
                            activity.finish();
                        } else {
                            AllFunction.storeToSharedPrefCount(mContext, "countingArray", countingArray);
                            Intent planningIntent = new Intent(mContext, Planning.class);
                            mContext.startActivity(planningIntent);
                            activity.finish();
                        }
                    }else{
                        Intent SyncIntent = new Intent(mContext, SyncData.class);
                        mContext.startActivity(SyncIntent);
                        activity.finish();
                    }
//                    SaveToSQLite saveToSQLite = new SaveToSQLite(strReason[0], activity, mContext, intDuty);
//                    saveToSQLite.ProcessToSQLite();
                    break;
            }
        }
    }
    public static void uploadSync() {
        UploadPlanData uploadPlanData = new UploadPlanData();
        uploadPlanData.setJobID(AllFunction.getIntFromSharedPref(mContext, Preference.prefJobID));
        uploadPlanData.setTaskID(AllFunction.getIntFromSharedPref(mContext, Preference.prefTaskID));
        uploadPlanData.setActualStart(AllFunction.getStringFromSharedPref(mContext, Preference.prefActualStart));
        uploadPlanData.setActualEnd(AllFunction.getStringFromSharedPref(mContext, Preference.prefActualEnd));
        uploadPlanData.setReasonState(0);
        uploadPlanData.setReasonID(0);
        uploadPlanData.setDoneStatus(AllFunction.getStringFromSharedPref(mContext, Preference.prefDoneStatus));
        List<UploadPlanData> lstUpload = new ArrayList<>();
        lstUpload.add(uploadPlanData);
        AllUploadData.getInstance().setUploadData(lstUpload);
        UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance());
    }
}
