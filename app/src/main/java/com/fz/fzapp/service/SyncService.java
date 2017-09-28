package com.fz.fzapp.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.common.Planning;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.ReasonData;
import com.fz.fzapp.data.TaskList;
import com.fz.fzapp.pojo.ReasonPojo;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.sending.ReasonHolder;
import com.fz.fzapp.sending.TaskListHolder;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Dibuat oleh : ignat
 * Tanggal : 05-Sep-17
 * HP/WA : 0857 7070 6 777
 */
public class SyncService {
    TextView tvMsg, tvNotifeSync;
    Button btnCancelGo;
    ImageView ivGo;
    static String TAG = "[SyncService]";
    private PopupMessege popupMessege = new PopupMessege();
    private Activity activity;
    private Context context;

    public SyncService(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.tvNotifeSync = (TextView) activity.findViewById(R.id.tvNotifeSync);
        this.tvMsg = (TextView) activity.findViewById(R.id.tvMsg);
        this.btnCancelGo = (Button) activity.findViewById(R.id.btnCancelGo);
        this.ivGo = (ImageView) activity.findViewById(R.id.ivGo);
    }

    public void getTaskList() {
        if (CheckConnection() == -1) return;

        TaskList taskList = new TaskList();
        taskList.setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
        taskList.setVehicleID(AllFunction.getIntFromSharedPref(context, Preference.prefVehicleID));
        TaskListHolder taskListHolder = new TaskListHolder(taskList);
        DataLink dataLink = AllFunction.BindingData();

        final Call<TaskListPojo> ReceivePojo = dataLink.TaskListService(taskListHolder);

        ReceivePojo.enqueue(new Callback<TaskListPojo>() {
            @Override
            public void onResponse(Call<TaskListPojo> call, Response<TaskListPojo> response) {
                if (response.isSuccessful()) {

                    if (response.body().getCoreResponse().getCode() != FixValue.intSuccess)
//						setAllOff(response.body().getCoreResponse().getMsg());
                        ProccessWait(response.body().getCoreResponse().getMsg());
                    else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                        AllUploadData.initUser();
                        AllFunction.storeToSharedPref(context, 1, Preference.prefDutyTask);
                        AllTaskList_adapter.getInstance().setAlltaskList(response.body().getTaskListResponse());
                        ProcessReasonFail();
                    }
                } else {
                    setAllOff(context.getResources().getString(R.string.msgServerData));
                }
            }

            @Override
            public void onFailure(Call<TaskListPojo> call, Throwable t)

            {
                Log.d("message", "qwerty2");

                setAllOff(context.getResources().getString(R.string.msgServerFailure));
            }
        });
    }

    private void ProcessReasonFail() {
        if (CheckConnection() == -1) return;

        ReasonData reasonData = new ReasonData();
        reasonData.setReasonID(1);

        ReasonHolder reasonHolder = new ReasonHolder(reasonData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<ReasonPojo> ReceivePojo = dataLink.ReasonListService(reasonHolder);

        ReceivePojo.enqueue(new Callback<ReasonPojo>() {
            @Override
            public void onResponse(Call<ReasonPojo> call, Response<ReasonPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCoreResponse().getCode() == FixValue.intFail)
                        setAllOff(response.body().getCoreResponse().getMsg());
                    else {
                        AllTaskList_adapter.getInstance().setAllresponsefail(response.body().getReasonResponse());
                        ProcessReasonLate();
                    }
                } else
                    setAllOff(context.getResources().getString(R.string.msgServerData));
            }

            @Override
            public void onFailure(Call<ReasonPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerFailure));
            }
        });
    }

    private void ProcessReasonLate() {
        if (CheckConnection() == -1) return;

        ReasonData reasonData = new ReasonData();
        reasonData.setReasonID(2);

        ReasonHolder reasonHolder = new ReasonHolder(reasonData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<ReasonPojo> ReceivePojo = dataLink.ReasonListService(reasonHolder);

        ReceivePojo.enqueue(new Callback<ReasonPojo>() {
            @Override
            public void onResponse(Call<ReasonPojo> call, Response<ReasonPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCoreResponse().getCode() == FixValue.intFail)
                        setAllOff(response.body().getCoreResponse().getMsg());
                    else {
                        AllTaskList_adapter.getInstance().setAllresponselate(response.body().getReasonResponse());
                        Intent NamaUserIntent = new Intent(activity, Planning.class);
                        context.startActivity(NamaUserIntent);
                        activity.finish();
                    }
                } else
                    setAllOff(context.getResources().getString(R.string.msgServerData));
            }

            @Override
            public void onFailure(Call<ReasonPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerFailure));
            }
        });
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            setAllOff(context.getResources().getString(R.string.msgServerResponse));
            return -1;
        }

        return 0;
    }

    private void setAllOff(String strMsg) {
        tvMsg.setText(context.getResources().getString(R.string.titleKlik));
        ivGo.setBackgroundResource(R.drawable.buttonthick);
        popupMessege.ShowMessege1(context, strMsg);
    }

    private void ProccessWait(String msg) {
        tvNotifeSync.setVisibility(View.VISIBLE);
        tvNotifeSync.setText(msg);
        ivGo.setImageResource(R.drawable.buttonthick);
    }
}