package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.R;
import com.fz.fzapp.model.TaskListResponse;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;

public class Planning extends AppCompatActivity {

    @BindView(R.id.tvPlanning1)
    TextView tvPlanning1;
    @BindView(R.id.tvPlanning2)
    TextView tvPlanning2;
    @BindView(R.id.btnCircle)
    RelativeLayout btnCircle;
    @BindView(R.id.tvPlanningTime)
    TextView tvPlanningTime;
    @BindView(R.id.tvPlanningTimeTo)
    TextView tvPlanningTimeTo;
    @BindView(R.id.tvPlanningFrom)
    TextView tvPlanningFrom;
    @BindView(R.id.tvPlanningTo)
    TextView tvPlanningTo;
    @BindView(R.id.planningKraniRequseter)
    TextView tvplanningKraniRequseter;
    @BindView(R.id.planningEstimate)
    TextView tvPlanningEstimate;
    @BindView(R.id.planningWeight)
    TextView tvPlanningWeight;
    @BindView(R.id.planningDivisi)
    TextView tvplanningDivisi;
    @BindView(R.id.planningBlocks)
    TextView tvplanningBlocks;
    @BindView(R.id.planningBin)
    TextView tvplanningBin;
    @BindView(R.id.planningEmptyBin)
    TextView tvplanningEmptyBin;
    @BindView(R.id.tvDivision)
    TextView tvDivision;

    private int lastActivity;
    private Activity activity = this;
    static String TAG = "[SinkronData]";
    private Context context = this;
    private Integer iTotalTask;
    private List<TaskListResponse> listDataTask;
    String startEstimate, endEstimate;
    Calendar cal;
    private int CountingArray;
    List<TaskListResponse> taskListResponses = null;
    private PopupMessege popupMessege = new PopupMessege();
    private static MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_lay);
        ButterKnife.bind(this);
        lastActivity = 1;
        ringtone();
        AllFunction.storeToSharedPref(context, lastActivity, Preference.prefLastActivity);
        CountingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
        taskListResponses = getPreferenceReason(Preference.prefTaskList);
        RetriveContents();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick({R.id.btnCircle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//      case R.id.ivOtherMenuPlan:
//        OtherOption otherOption = new OtherOption(context, activity, Username.class);
//        otherOption.ShowOtherOptions();
//      break;

            case R.id.btnCircle:
                popupMessege.ShowMessege7(context, "Anda Yakin ?", Planning.this, Duty.class, player);
                break;
        }
    }

    private List<TaskListResponse> getPreferenceReason(String key) {
        SharedPreferences prefs = getSharedPreferences(key, 1);
        String httpParamJSONList = prefs.getString(key, "");
        List<TaskListResponse> httpParamList = new Gson().fromJson(httpParamJSONList, new TypeToken<List<TaskListResponse>>() {
        }.getType());
        return httpParamList;
    }


    private void ringtone() {
        if (FixValue.Buzzer == 0) {
            Uri notification = Uri.parse("android.resource://com.fz.fzapp/raw/alarm_alarm_alarm");
            player = MediaPlayer.create(context, notification);
            player.setLooping(true);
            FixValue.Buzzer = 1;
            player.start();
        } else if (FixValue.Buzzer == 1) {
            player.stop();
            FixValue.Buzzer = 0;
        }
    }

    private void RetriveContents() {
        TaskListPojo taskListPojo = new TaskListPojo();
        taskListPojo = AllFunction.getObjectFromSharedPrefObj(context, TaskListPojo.class, Preference.prefAllTaskList);
        startEstimate = taskListPojo.getTaskListResponse().get(CountingArray).getStart();
        endEstimate = taskListPojo.getTaskListResponse().get(CountingArray).getEnd();
        AllFunction.storeToSharedPref(context, taskListPojo.getTaskListResponse().get(CountingArray).getJobID(), Preference.prefJobID);
        AllFunction.storeToSharedPref(context, taskListPojo.getTaskListResponse().get(CountingArray).getTaskID(), Preference.prefTaskID);
        AllFunction.storeToSharedPref(context, "DONE", Preference.prefDoneStatus);
        String starTime = AllFunction.getTime(startEstimate);
        String EndTime = AllFunction.getTime(endEstimate);
        int Estimation = AllFunction.reductionDate(startEstimate, endEstimate);
        tvDivision.setText(AllFunction.getStringFromSharedPref(context, Preference.prefTruckName));
        tvplanningKraniRequseter.setText(taskListPojo.getTaskListResponse().get(CountingArray).getName());
        tvPlanningEstimate.setText(String.valueOf(Estimation) + " menit");
        tvPlanningWeight.setText(taskListPojo.getTaskListResponse().get(CountingArray).getTonnage() + " kg");
        tvplanningDivisi.setText(taskListPojo.getTaskListResponse().get(CountingArray).getDivID());
        tvplanningBlocks.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBlocks());
        tvPlanningTime.setText(starTime);
        tvPlanningTimeTo.setText(EndTime);
        tvPlanningFrom.setText(taskListPojo.getTaskListResponse().get(CountingArray).getFrom());
        tvPlanningTo.setText(taskListPojo.getTaskListResponse().get(CountingArray).getTo());
        tvplanningBin.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBinLocation());
        tvplanningEmptyBin.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBinEmpty());

    }

}

