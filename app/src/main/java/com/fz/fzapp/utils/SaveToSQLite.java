package com.fz.fzapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.common.Recap;
import com.fz.fzapp.model.TaskListResponse;
import com.fz.fzapp.service.AllFunction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Dibuat oleh : ignat
 * Tanggal : 03-Sep-17
 * HP/WA : 0857 7070 6 777
 */
public class SaveToSQLite
{
	private String Notes;
	private Activity activity;
	private Context context;
	private Integer intDuty;

	public SaveToSQLite(String Notes, Activity activity, Context context, Integer intDuty)
	{
		this.Notes = Notes;
		this.activity = activity;
		this.context = context;
		this.intDuty = intDuty;
	}

	public void ProcessToSQLite()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.US);

		Integer TaskNo = AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask);
		Integer TaskSeq = TaskNo - 1;
		Integer intTaskID = AllTaskList_adapter.getInstance().getAlltaskList().get(TaskSeq).getTaskID();

		TaskListResponse UpdateTaskList = new TaskListResponse();
		UpdateTaskList.setTaskID(intTaskID);
//		UpdateTaskList.setDisplayName(AllTaskList_adapter.getInstance().getAlltaskList().get(TaskSeq).getDisplayName());
//		UpdateTaskList.setDescription(AllTaskList_adapter.getInstance().getAlltaskList().get(TaskSeq).getDescription());
//		UpdateTaskList.setEstimateDate(AllTaskList_adapter.getInstance().getAlltaskList().get(TaskSeq).getEstimateDate());
//		UpdateTaskList.setEstimateTime(AllTaskList_adapter.getInstance().getAlltaskList().get(TaskSeq).getEstimateTime());
//		UpdateTaskList.setActualdate(df.format(calendar.getTime()));
//		UpdateTaskList.setActualtime(tf.format(calendar.getTime()));
//		UpdateTaskList.setStatustask(1);

		AllTaskList_adapter.getInstance().getAlltaskList().set(TaskSeq, UpdateTaskList);

		Integer lnkUserID = AllFunction.getIntFromSharedPref(context, Preference.prefUserID);

		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.clear();

		hashMap.put("lnkUserID", String.valueOf(lnkUserID));
		hashMap.put("lnkTaskID", String.valueOf(intTaskID));
		hashMap.put("lnkLocationID", String.valueOf(1));
		hashMap.put("lnkReasonID", String.valueOf(intDuty));
		hashMap.put("Notes", Notes);

		Database_adapter adapter_database = new Database_adapter(context);
		if(adapter_database.SaveReportData(hashMap))
		{
			AllFunction.storeToSharedPref(context, TaskNo + 1, Preference.prefDutyTask);
			Intent RecapIntent = new Intent(activity, Recap.class);
			context.startActivity(RecapIntent);
			activity.finish();
		}
	}
}
