package com.fz.fzapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by aignatd on 14-Aug-16.
 */
public class Database_adapter extends SQLiteOpenHelper
{
  static String TAG = "[AdapterDatabase]";
  public static final String databasename = "tasklist.sqlite";
  public static final String databaselocation = "/data/data/com.fz.fzapp/databases/";

  public static final String LocationTable = "tracking";
  public static final String ReportTable = "trxtasklist";

  private Context mContext;
  private SQLiteDatabase sqlDatabase;
  private SQLiteDatabase sqlDatabaseSync;

  public Database_adapter(Context context)
  {
    super(context, databasename, null, 1);
    this.mContext = context;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase)
  {

  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
  {

  }

  public boolean SaveTrackingData(HashMap<String, String> TrackingData)
  {
    long lNewPos = -1;

    if(OpenDatabase())
    {
      ContentValues values = new ContentValues();
      sqlDatabase.beginTransaction();

      values.put("Latitude", TrackingData.get("Latitude"));
      values.put("Longitude", TrackingData.get("Longitude"));
      values.put("Date", TrackingData.get("Date"));
      values.put("Time", TrackingData.get("Time"));
      lNewPos = sqlDatabase.insert(LocationTable, null, values);

      sqlDatabase.setTransactionSuccessful();
      sqlDatabase.endTransaction();
    }

    CloseDatabase();

    if(lNewPos < 0)
      return false;
    else
      return true;
  }

  public boolean SaveReportData(HashMap<String, String> TrxData)
  {
    long NewPos = -1;

    if(OpenDatabase())
    {
      ContentValues values = new ContentValues();
      sqlDatabase.beginTransaction();

      values.put("lnkUserID", TrxData.get("lnkUserID"));
      values.put("lnkTaskID", TrxData.get("lnkTaskID"));
      values.put("lnkLocationID", TrxData.get("lnkLocationID"));
      values.put("lnkReasonID", TrxData.get("lnkReasonID"));
      values.put("Notes", TrxData.get("Notes"));

      NewPos = sqlDatabase.insert(ReportTable, null, values);

      sqlDatabase.setTransactionSuccessful();
      sqlDatabase.endTransaction();
    }

    CloseDatabase();

    if(NewPos < 0)
      return false;
    else
      return true;
  }

  public boolean SearchTaskListByID(long TaskListID)
  {
    boolean LastStatus = false;
    Cursor cursor = null;

    if(OpenDatabase())
    {
      cursor = sqlDatabase.rawQuery("SELECT * FROM " + ReportTable + " WHERE lnkTaskID=?",
                      new String[] {Long.toString(TaskListID)});

      if (cursor.getCount() == 0)
        LastStatus = false;
      else
        LastStatus = true;
    }

    if(cursor == null) LastStatus = false;

    CloseDatabase();
    return LastStatus;
  }

  public int GetLastLocationID()
  {
    Cursor cursor;
    int LastCursor = -1;

    if(OpenDatabase())
    {
      cursor = sqlDatabase.rawQuery("SELECT MAX(LocationID) FROM tracking", null);
      cursor.moveToFirst();
      LastCursor = cursor.getInt(0);
    }

    CloseDatabase();
    return LastCursor;
  }

  public Cursor GetAllRecordToSync(String NameOfTable)
  {
    Cursor cursor = null;

    if(OpenDatabaseSync())
    {
      cursor = sqlDatabaseSync.rawQuery("SELECT * FROM " + NameOfTable + " WHERE Status=0", null);
    }

    return cursor;
  }

  public boolean UpdateSyncTaskList(HashMap<String, String> SyncTrxData)
  {
    long NewPos = -1;

    Log.d(TAG, "UpdateSyncTaskList: 1");

    if(OpenDatabase())
    {
      Log.d(TAG, "UpdateSyncTaskList: 2");
      ContentValues values = new ContentValues();
      sqlDatabase.beginTransaction();

      Log.d(TAG, "UpdateSyncTaskList: 3");
      values.put("Status", SyncTrxData.get("Status"));
      NewPos = sqlDatabase.update(ReportTable, values, "ReportID=" + Integer.valueOf(SyncTrxData.get("SyncID")), null);

      Log.d(TAG, "UpdateSyncTaskList: 4");
      sqlDatabase.setTransactionSuccessful();
      sqlDatabase.endTransaction();
      Log.d(TAG, "UpdateSyncTaskList: 5");
    }

    CloseDatabase();
    Log.d(TAG, "UpdateSyncTaskList: " + NewPos);

    if(NewPos < 0)
      return false;
    else
      return true;
  }

  private boolean OpenDatabase()
  {
    String DataBasePath = mContext.getDatabasePath(databasename).getPath();
    if((sqlDatabase != null) && (sqlDatabase.isOpen())) return false;
    sqlDatabase = SQLiteDatabase.openDatabase(DataBasePath, null, SQLiteDatabase.OPEN_READWRITE);
    return true;
  }

  private void CloseDatabase()
  {
    if(sqlDatabase != null) sqlDatabase.close();
  }

  private boolean OpenDatabaseSync()
  {
    String DataBasePath = mContext.getDatabasePath(databasename).getPath();
    if((sqlDatabaseSync != null) && (sqlDatabaseSync.isOpen())) return false;
    sqlDatabaseSync = SQLiteDatabase.openDatabase(DataBasePath, null, SQLiteDatabase.OPEN_READWRITE);
    return true;
  }

  public void CloseDatabaseSync()
  {
    if(sqlDatabaseSync != null) sqlDatabaseSync.close();
  }
}
