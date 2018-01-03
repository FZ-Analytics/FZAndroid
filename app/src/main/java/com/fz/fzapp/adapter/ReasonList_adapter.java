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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.fz.fzapp.R;
import com.fz.fzapp.common.Planning;
import com.fz.fzapp.common.SyncData;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;
import com.fz.fzapp.model.ReasonResponse;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.popup.ReasonStatus;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.UploadData;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.SaveToSQLite;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static PopupMessege popupMessege = new PopupMessege();

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
        vhView.tvReasonName.setText(reasonResponses.get(i).getReasondesc());
        vhView.tvReasonName.setTag(reasonResponses.get(i).getReasonid());

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
                    Integer rsnId = (Integer) tvReasonName.getTag();
                    TaskListPojo taskListPojo = new TaskListPojo();
                    taskListPojo = AllFunction.getObjectFromSharedPrefObj(mContext, TaskListPojo.class, Preference.prefAllTaskList);
                    if (intDuty == 2) {
                        //Late
                        int countingArray = AllFunction.getIntFromSharedPref(mContext, "countingArray");
                        if (taskListPojo.getTaskListResponse().size() == countingArray + 1) {
                            //Succsecc once job
                            AllFunction.uploadSync(2, mContext, rsnId);
//                            popupMessege.ShowMessegeUpload(mContext, activity);
                            UploadDataJob();
                        } else {
                            //success half job
                            AllFunction.uploadSync(2, mContext, rsnId);
                            AllFunction.storeToSharedPrefCount(mContext, "countingArray", countingArray);
                            Intent planningIntent = new Intent(mContext, Planning.class);
                            mContext.startActivity(planningIntent);
                            activity.finish();
                        }
                    } else {
                        //fail
                        AllFunction.uploadSync(1, mContext, rsnId);
                        Intent SyncIntent = new Intent(mContext, SyncData.class);
                        mContext.startActivity(SyncIntent);
                        activity.finish();

                    }
                    break;
            }
        }

        private void UploadDataJob() {
            final Gson gson = new Gson();
            AllUploadData allUploadData = AllFunction.getObjectFromSharedPrefObj(mContext, AllUploadData.class, Preference.prefAllUploadData);
//            UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance().getUploadData());
            UploadHolder uploadHolder = new UploadHolder(allUploadData.getUploadData(), 9000);
            Log.d("Test", "getUploadHolder : " + gson.toJson(uploadHolder));

            if (CheckConnection() == -1) return;
            DataLink dataLink = AllFunction.BindingData();
            final Call<UploadPojo> ReceiveUpload = dataLink.uploadService(uploadHolder);
            ReceiveUpload.enqueue(new Callback<UploadPojo>() {
                @Override
                public void onResponse(Call<UploadPojo> call, Response<UploadPojo> response) {
                    if ((response.body().getCoreResponse().getCode() == FixValue.intSuccess)) {
                        String msg = response.body().getCoreResponse().getMsg();
                        Intent SyncIntent = new Intent(mContext, SyncData.class);
                        mContext.startActivity(SyncIntent);
                        onProcessSyncData();
                        activity.finish();
                    } else {
                        Toast.makeText(mContext, response.body().getCoreResponse().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadPojo> call, Throwable t) {

                }
            });
        }

        private Integer CheckConnection() {
            if (AllFunction.isNetworkAvailable(mContext) == FixValue.TYPE_NONE) {
                Toast.makeText(mContext, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
//                setAllOff(mContext.getResources().getString(R.string.msgCheckConnection));
                return -1;
            }
            return 0;
        }

        private void setAllOff(String strMsg) {
////          tvMsg.setText(context.getResources().getString(R.string.titleKlik));
//            ivGo.setBackgroundResource(R.drawable.buttonthick);
//            popupMessege.ShowMessege1(mContext, strMsg);
        }
    }

    private static void onProcessSyncData() {
        HashMap<String, String> listSyncTable = new HashMap<>();
        listSyncTable.clear();

        listSyncTable.put("tracking", "tracking");

        new UploadData(activity, mContext, listSyncTable).execute();
    }

}
