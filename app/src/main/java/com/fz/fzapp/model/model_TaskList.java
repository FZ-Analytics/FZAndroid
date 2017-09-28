package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class model_TaskList
{
  @SerializedName("TaskID")
  @Expose
  private Integer TaskID;
  @SerializedName("Task")
  @Expose
  private String Task;
  @SerializedName("ExpectedTime")
  @Expose
  private String ExpectedTime;
  @SerializedName("ActualTime")
  @Expose
  private String ActualTime;

  public Integer getTaskID()
  {
    return TaskID;
  }

  public void setTaskID(Integer taskID)
  {
    TaskID = taskID;
  }

  public String getTask()
  {
    return Task;
  }

  public void setTask(String task)
  {
    Task = task;
  }

  public String getExpectedTime()
  {
    return ExpectedTime;
  }

  public void setExpectedTime(String expectedTime)
  {
    ExpectedTime = expectedTime;
  }

  public String getActualTime()
  {
    return ActualTime;
  }

  public void setActualTime(String actualTime)
  {
    ActualTime = actualTime;
  }
}
