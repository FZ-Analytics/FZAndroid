package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.data.LogoutData;
import com.fz.fzapp.pojo.LogoutPojo;
import com.fz.fzapp.sending.LogoutHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.SyncService;
import com.fz.fzapp.service.UploadData;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SyncData extends AppCompatActivity {
    //    @BindView(R.id.ivOtherMenu)
//    ImageView ivOtherMenu;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.tvNotifeSync)
    TextView tvNotifeSync;
    @BindView(R.id.btnCancelGo)
    Button btnCancelGo;
    @BindView(R.id.ivGo)
    ImageView ivGo;
    @BindView(R.id.tvUserNameSync)
    TextView tvUserNameSync;
    @BindView(R.id.rlProgreeBar)
    RelativeLayout progressBar;

    @BindView(R.id.tvTruckSync)
    TextView tvTruckSync;
    private int lastActivity;
    private Timer myTimer;
    private Activity activity = this;
    static String TAG = "[SyncData]";
    private Context context = this;
    private int firstCount = -1;
    private PopupMessege popupMessege = new PopupMessege();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syncdata_lay);
        ButterKnife.bind(this);
        lastActivity = 0;
        AllFunction.storeToSharedPref(context, lastActivity, Preference.prefLastActivity);
        FixValue.Buzzer = 0;
        tvUserNameSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefFullName));
        tvTruckSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefTruckName));
        AllFunction.storeToSharedPrefCount(context, "countingArray", firstCount);
        timerforNotification();
        AllTaskList_adapter.initAllTaskList();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick({R.id.ivGo, R.id.btnCancelGo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivGo:
                ivGo.setEnabled(false);
                int checkStatus = 0;
                tvMsg.setText(context.getResources().getString(R.string.strWait));
                btnCancelGo.setVisibility(View.VISIBLE);
                ivGo.setImageResource(R.drawable.buttongo);
                tvNotifeSync.setVisibility(view.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                SyncService syncData = new SyncService(activity, context, checkStatus, myTimer, progressBar);
                syncData.getTaskList();
                break;
            case R.id.btnCancelGo:

                RetriveContent();
                break;
        }
    }

    private void RetriveContent() {
        if (CheckConnection() == -1) return;
        LogoutData logoutData = new LogoutData();
        logoutData.setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
        LogoutHolder logoutHolder = new LogoutHolder(logoutData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<LogoutPojo> ReceiveLogout = dataLink.LogoutService(logoutHolder);

        ReceiveLogout.enqueue(new Callback<LogoutPojo>() {
            @Override
            public void onResponse(Call<LogoutPojo> call, Response<LogoutPojo> response) {
                Log.d("TaskList", response.body().getCoreResponse().getMsg() + response.body().getCoreResponse().getCode());
                if (response.body().getCoreResponse().getCode() != FixValue.intSuccess)
                    ProccessWait(response.body().getCoreResponse().getMsg());
                else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                    myTimer.cancel();
                    AllFunction.storeToSharedPref(context, 0, Preference.prefCheckLogin);
                    popupMessege.ShowMessege6(context, "Anda Yakin ?", SyncData.this, Username.class);
                }
            }

            @Override
            public void onFailure(Call<LogoutPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerData));
            }
        });
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
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

    private void timerforNotification() {
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
                Log.d("CheckTimer", "tick");
            }

        }, 0, 60000);
    }

    private void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
//            myTimer.cancel();
            Log.d("Notification", "check Notfication");
            int checkStatus = 1;
            SyncService syncData = new SyncService(activity, context, checkStatus, myTimer, progressBar);
            syncData.getTaskList();
        }
    };

    private void onProcessSyncData() {
        HashMap<String, String> listSyncTable = new HashMap<>();
        listSyncTable.clear();
        listSyncTable.put("tracking", "tracking");
        new UploadData(activity, context, listSyncTable).execute();
    }
}
