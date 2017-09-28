package com.fz.fzapp.sending;


import com.fz.fzapp.data.TaskListTrx;
import com.fz.fzapp.data.TrackingTrx;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class SyncTrxHolder
{
  private String TableName;
  private TaskListTrx TaskListTrxData;
  private TrackingTrx TrackingTrxData;

  public SyncTrxHolder(String tableName, TaskListTrx taskListTrxData, TrackingTrx trackingTrxData)
  {
    TableName = tableName;
    TaskListTrxData = taskListTrxData;
    TrackingTrxData = trackingTrxData;
  }
}
