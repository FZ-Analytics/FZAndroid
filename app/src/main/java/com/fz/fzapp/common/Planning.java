package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.model.TaskListResponse;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.fz.fzapp.service.AllFunction.getDate;

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
    @BindView(R.id.planningDate)
    TextView tvPlanningDate;
    @BindView(R.id.planningEstimate)
    TextView tvPlanningEstimate;
    @BindView(R.id.planningWeight)
    TextView tvPlanningWeight;
    @BindView(R.id.btnViewDetail)
    ImageView btnViewDetail;
    @BindView(R.id.llViewDetails)
    LinearLayout llViewDetails;


    private Activity activity = this;
    static String TAG = "[SinkronData]";
    private Context context = this;
    private Integer iTotalTask;
    private List<TaskListResponse> listDataTask;
    String startEstimate, endEstimate;
    private int countArray = 0;
    private int countdetails = 0;
    Calendar cal;
    private SimpleDateFormat tf, df;
    private int CountingArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planning_lay);
        ButterKnife.bind(this);
        CountingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
        RetriveContent();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick({R.id.btnCircle, R.id.btnViewDetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//      case R.id.ivOtherMenuPlan:
//        OtherOption otherOption = new OtherOption(context, activity, Username.class);
//        otherOption.ShowOtherOptions();
//      break;
            case R.id.btnCircle:
                Intent DutyIntent = new Intent(Planning.this, Duty.class);
                startActivity(DutyIntent);
                finish();
                break;
            case R.id.btnViewDetail:
                if (countdetails == 0) {
                    llViewDetails.setVisibility(View.VISIBLE);
                    countdetails = 1;
                    btnViewDetail.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                } else {
                    llViewDetails.setVisibility(View.GONE);
                    countdetails = 0;
                    btnViewDetail.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                }
                break;
//      case R.id.btnEndTask:
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder
//          .setTitle(R.string.titleMessege)
//          .setMessage(context.getResources().getString(R.string.msgContinueSync))
//          .setIcon(android.R.drawable.ic_dialog_alert)
//          .setCancelable(false)
//          .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener()
//          {
//            public void onClick(DialogInterface dialog, int which)
//            {
//              onProcessSyncData();
//            }
//          })
//          .setNegativeButton(R.string.strBtnCancel, new DialogInterface.OnClickListener()
//          {
//            public void onClick(DialogInterface dialog, int id)
//            {
//              dialog.cancel();
//            }
//          });
//
//        AlertDialog alert = builder.create();
//        alert.show();
//      break;
        }
    }

    private void RetriveContent() {
        startEstimate = AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getStart();
        endEstimate = AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getEnd();
        AllFunction.storeToSharedPref(context, AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getJobID(), Preference.prefJobID);
        AllFunction.storeToSharedPref(context, AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getTaskID(), Preference.prefTaskID);
        AllFunction.storeToSharedPref(context, "DONE", Preference.prefDoneStatus);

        String StartDate = getDate(startEstimate);
        String starTime = AllFunction.getTime(startEstimate);
        String EndDate = AllFunction.getDate(endEstimate);
        String EndTime = AllFunction.getTime(endEstimate);
        int Estimation = AllFunction.reductionDate(startEstimate, endEstimate);

        tvPlanningDate.setText(StartDate);
        tvPlanningEstimate.setText(String.valueOf(Estimation) + " menit");
        tvPlanningWeight.setText(AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getTonnage() + " kg");
        tvPlanningTime.setText(starTime);
        tvPlanningTimeTo.setText(EndTime);
        tvPlanningFrom.setText(AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getFrom());
        tvPlanningTo.setText(AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getBlocks());
    }


}


//  private void getAllTaskList()
//  {
////    lvListAll.invalidateViews();
//    TaskList_adapter taskList_adapter = new TaskList_adapter(activity, context, AllTaskList_adapter.getInstance().getAlltaskList());
////    lvListAll.setAdapter(taskList_adapter);
//
//    TaskListData taskListData = (TaskListData) taskList_adapter.getItem(taskList_adapter.getCount() - 1);
//    iTotalTask = taskList_adapter.getCount();
//
//    AllFunction.storeToSharedPref(context, taskListData.getSyncDate(), Preference.prefSyncDate);
//    AllFunction.storeToSharedPref(context, taskListData.getSyncTime(), Preference.prefSyncTime);
//
//    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > 1)
//    {
//      tvPlanning1.setText(context.getResources().getString(R.string.titlePlan3));
//      tvPlanning2.setText(context.getResources().getString(R.string.titlePlan4));
//    }else
//      tvPlanning2.setText(context.getResources().getString(R.string.titlePlan2));
//
////    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > 1)
////      btnEndTask.setVisibility(View.VISIBLE);
//
//    if(AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask) > iTotalTask)
//      btnCircle.setVisibility(View.GONE);
//  }
//
//  private void onProcessSyncData()
//  {
//    HashMap<String, String> listSyncTable = new HashMap<>();
//    listSyncTable.clear();
//
//    listSyncTable.put("Tracking", "tracking");
//    listSyncTable.put("TaskList", "trxtasklist");
//
//    new UploadData(activity, context, listSyncTable).execute();
//  }

