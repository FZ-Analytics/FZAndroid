package com.fz.fzapp.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.common.Planning;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.ReasonData;
import com.fz.fzapp.data.TaskList;
import com.fz.fzapp.model.TaskListResponse;
import com.fz.fzapp.pojo.ReasonPojo;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.sending.ReasonHolder;
import com.fz.fzapp.sending.TaskListHolder;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.NOTIFICATION_SERVICE;

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
    private int status;
    private RelativeLayout progressBar;
    private Timer timer;
    private Ringtone ringtone;
    private Uri ringtoneUri;
    private static MediaPlayer player;

    public SyncService(Activity activity, Context context, int CheckStatus, Timer myTimer, RelativeLayout progressBar) {
        this.activity = activity;
        this.context = context;
        this.status = CheckStatus;
        this.tvNotifeSync = (TextView) activity.findViewById(R.id.tvNotifeSync);
        this.tvMsg = (TextView) activity.findViewById(R.id.tvMsg);
        this.ivGo = (ImageView) activity.findViewById(R.id.ivGo);
        this.timer = myTimer;
        this.progressBar = progressBar;
    }

    public void getTaskList() {
        if (CheckConnection() == -1) return;

        final TaskList taskList = new TaskList();
        taskList.setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
        taskList.setVehicleID(AllFunction.getIntFromSharedPref(context, Preference.prefVehicleID));
        TaskListHolder taskListHolder = new TaskListHolder(taskList);
        DataLink dataLink = AllFunction.BindingData();

        final Call<TaskListPojo> ReceivePojo = dataLink.TaskListService(taskListHolder);

        ReceivePojo.enqueue(new Callback<TaskListPojo>() {
            @Override
            public void onResponse(Call<TaskListPojo> call, Response<TaskListPojo> response) {
                ivGo.setEnabled(true);
                if (response.isSuccessful()) {
                    Log.d("TaskList", response.body().getCoreResponse().getMsg() + response.body().getCoreResponse().getCode());
                    if (status == 0) {
                        //From Button
                        if (response.body().getCoreResponse().getCode() != FixValue.intSuccess) {
                            progressBar.setVisibility(View.GONE);
                            ProccessWait(response.body().getCoreResponse().getMsg());
                        } else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {

                            List<TaskListResponse> gsonFail = response.body().getTaskListResponse();
                            TaskListPojo taskListPojo = response.body();
                            AllFunction.storeObjectToSharedPrefObj(context, taskListPojo, Preference.prefAllTaskList);
                            timer.cancel();
                            progressBar.setVisibility(View.GONE);
                            Log.d("Balikan button", String.valueOf(response.body()));
                            AllUploadData.initUser();
                            AllFunction.storeToSharedPref(context, 1, Preference.prefDutyTask);
                            AllTaskList_adapter.getInstance().setAlltaskList(response.body().getTaskListResponse());

                            Intent NamaUserIntent = new Intent(activity, Planning.class);
                            context.startActivity(NamaUserIntent);
                            activity.finish();
                        }
                    } else {
                        //From Timmer
                        if (response.body().getCoreResponse().getCode() != FixValue.intSuccess) {
                            Log.d("MsgSync", "Belum ada task");
                        } else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                            timer.cancel();
                            notifyThis("Sinarmas", "Cek order");


                            AllUploadData.initUser();
                            AllFunction.storeToSharedPref(context, 1, Preference.prefDutyTask);
                            AllTaskList_adapter.getInstance().setAlltaskList(response.body().getTaskListResponse());
                            List<TaskListResponse> gsonFail = response.body().getTaskListResponse();
                            TaskListPojo taskListPojo = response.body();
                            AllFunction.storeObjectToSharedPrefObj(context, taskListPojo, Preference.prefAllTaskList);
                            Log.d("Balikan timmer", String.valueOf(response.body()));

                            Intent NamaUserIntent = new Intent(activity, Planning.class);
                            NamaUserIntent.putExtra("ringtone-uri", String.valueOf(player));
                            context.startActivity(NamaUserIntent);
                            activity.finish();
                        }
                    }
                } else {
                    if (status == 0) {
                        Toast.makeText(context, R.string.msgServerData, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("MsgSync", "Belum ada task");
                    }
                }
            }

            @Override
            public void onFailure(Call<TaskListPojo> call, Throwable t)
            {
                ivGo.setEnabled(true);
                if (status == 0) {
                    Log.d("message", "qwerty2");
                    Toast.makeText(context, R.string.msgServerFailure, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("MsgSync", "Belum ada task");
                }

            }
        });
    }


    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            if (status == 0) {
                Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();

            } else {
                Log.d("MsgSync", "Connecting Bye");
            }
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

    public void notifyThis(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //Define sound URI
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri soundUri = Uri.parse("android.resource://com.fz.fzapp/raw/alarm_alarm_alarm");
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context.getApplicationContext())
                .setSmallIcon(R.drawable.truck_32)
                .setContentTitle(title)
                .setContentText(message)
//                .setSound(soundUri)
                .setAutoCancel(true);


        Notification mNotification = mBuilder.build();
        mNotification.flags |= Notification.FLAG_INSISTENT;
        notificationManager.notify(1, mNotification);
    }

    private void storeToTaskList(List<TaskListResponse> gsonFail, String key) {
        String httpParamJSONList = new Gson().toJson(gsonFail);
        SharedPreferences prefs = context.getSharedPreferences(key, 1);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, httpParamJSONList);
        editor.apply();
    }

}