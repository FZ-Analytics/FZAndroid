package com.fz.fzapp.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.data.TaskListTrx;
import com.fz.fzapp.data.TrackingTrx;
import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.sending.SyncTrxHolder;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Agustinus Ignat on 15-Aug-17.
 */
public class UploadData extends AsyncTask<String, Void, String>
{
  private PopupMessege popupMessege = new PopupMessege();
  private String TAG = "[UploadData]";

  private Activity activity;
  private Context context;
  private HashMap<String, String> SQLTable;

  ProgressDialog progressDialog;
  Database_adapter adapter_database;

  public UploadData(Activity activity, Context context, HashMap<String, String> SQLTable)
  {
    this.context = context;
    this.SQLTable = SQLTable;
    this.activity = activity;
  }

  @Override
  protected void onPreExecute()
  {
    progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.strWait),
                                         context.getResources().getString(R.string.strSyncProgress));
    progressDialog.setCancelable(false);
    progressDialog.show();

    adapter_database = new Database_adapter(context);
  }

  @Override
  protected String doInBackground(String... strings)
  {
    String strResults = context.getResources().getString(R.string.strSyncFinish);
    int intResult = 0;
    Cursor cursor;

    for (String key : SQLTable.keySet())
    {
      String TableName = SQLTable.get(key);
      adapter_database.CloseDatabaseSync();

      cursor = adapter_database.GetAllRecordToSync(TableName);
      Log.d(TAG, key + " - Cursor -> " + cursor.getCount());

      if((cursor != null) && (cursor.getCount() > 0))
      {
        cursor.moveToFirst();
        intResult = 0;

        do
        {
          int RunSync = TransferSynDataToServer(TableName, cursor, adapter_database);
          Log.d(TAG, key + " - RunSync -> " + RunSync);

          if(RunSync == FixValue.intNoNetwork)
          {
            intResult = FixValue.intNoNetwork;
            break;
          }
          else
          if(RunSync == FixValue.intFail)
          {
            intResult = FixValue.intFail;
            break;
          }
        }
        while(cursor.moveToNext());
      }
      else
      if(cursor.getCount() == 0)
        intResult = FixValue.intNoData;
      else
      if(cursor == null)
        intResult = FixValue.intNull;

      Log.d(TAG, key + " - intResult -> " + intResult);
      Log.d(TAG, key + " - TableName -> " + TableName);

      if(intResult == FixValue.intNoNetwork)
        strResults = context.getResources().getString(R.string.msgServerFailure);
      else
      if(intResult == FixValue.intFail)
        strResults = context.getResources().getString(R.string.strUploadDataFailed);
      else
      if((intResult == FixValue.intNull) || (intResult == FixValue.intNoData))
        strResults = "Table " + key + " " + context.getResources().getString(R.string.strUploadData);
      else
      if(intResult == FixValue.intSuccess)
        strResults = context.getResources().getString(R.string.strSyncFinish);

      if((intResult == FixValue.intNoNetwork) || (intResult == FixValue.intFail) || (intResult == FixValue.intNull))
        break;
    }

    return strResults;
  }

  @Override
  protected void onPostExecute(String results)
  {
    super.onPostExecute(results);

    progressDialog.dismiss();
    popupMessege.ShowMessege1(context, results);

//    Intent UploadIntent = new Intent(activity, SyncData.class);
//    context.startActivity(UploadIntent);
//    activity.finish();
  }

  private int TransferSynDataToServer(String TableName, Cursor cursor, Database_adapter adapter_database)
  {
    if(AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE)
      return FixValue.intNoNetwork;

    TaskListTrx taskListTrx = new TaskListTrx();
    TrackingTrx trackingTrx = new TrackingTrx();

    final int SyncID = cursor.getInt(0);

    if(TableName.trim() == "trxtasklist")
    {
      taskListTrx.setReportID(SyncID);
      taskListTrx.setLnkUserID(cursor.getInt(1));
      taskListTrx.setLnkTaskID(cursor.getInt(2));
      taskListTrx.setLnkLocationID(cursor.getInt(3));
      taskListTrx.setLnkReasonID(cursor.getInt(4));
      taskListTrx.setNotes(cursor.getString(5));
      taskListTrx.setStatus(cursor.getInt(6));

      trackingTrx = null;
    }
    else
    if(TableName.trim() == "tracking")
    {
      trackingTrx.setLocationID(SyncID);
      trackingTrx.setLatitude(cursor.getString(1));
      trackingTrx.setLongitude(cursor.getString(2));
      trackingTrx.setDate(cursor.getString(3));
      trackingTrx.setTime(cursor.getString(4));
      trackingTrx.setStatus(cursor.getInt(5));

      taskListTrx = null;
    }

    SyncTrxHolder syncTrxHolder = new SyncTrxHolder(TableName, taskListTrx, trackingTrx);
    DataLink dataLink = AllFunction.BindingData();

    final Call<LoginPojo> ReceivePojo = dataLink.SyncTrxService(syncTrxHolder);
    Response<LoginPojo> response = null;
    int resultSync;

    try
    {
      response = ReceivePojo.execute();
      resultSync = FixValue.intSuccess;
    }
    catch (IOException e)
    {
      resultSync = FixValue.intFail;
    }

    if(resultSync == FixValue.intSuccess)
    {
      if (response.isSuccessful())
      {
        if(response.body().getCoreResponse().getCode() == FixValue.intSuccess)
        {
          HashMap<String, String> hashMap = new HashMap<>();
          hashMap.clear();

          hashMap.put("SyncID", String.valueOf(SyncID));
          hashMap.put("Status", "1");

          if(adapter_database.UpdateSyncTaskList(hashMap))
            resultSync = FixValue.intSuccess;
          else
            resultSync = FixValue.intFail;
        }
        else
          resultSync = FixValue.intFail;
      }
      else
      {
        resultSync = FixValue.intFail;
      }
    }

    return resultSync;
  }
}
