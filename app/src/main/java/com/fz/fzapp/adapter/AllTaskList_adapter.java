package com.fz.fzapp.adapter;

import com.fz.fzapp.model.ReasonResponse;
import com.fz.fzapp.model.TaskListResponse;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 20-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class AllTaskList_adapter
{
  private List<TaskListResponse> alltaskList;
  private List<ReasonResponse> allresponsefail;
  private List<ReasonResponse> allresponselate;

  public List<ReasonResponse> getAllresponsefail()
  {
    return allresponsefail;
  }

  public void setAllresponsefail(List<ReasonResponse> allresponsefail)
  {
    this.allresponsefail = allresponsefail;
  }

  public List<ReasonResponse> getAllresponselate()
  {
    return allresponselate;
  }

  public void setAllresponselate(List<ReasonResponse> allresponselate)
  {
    this.allresponselate = allresponselate;
  }

  public List<TaskListResponse> getAlltaskList()
  {
    return alltaskList;
  }

  public void setAlltaskList(List<TaskListResponse> alltaskList)
  {
    this.alltaskList = alltaskList;
  }

  private static AllTaskList_adapter UserInstance = new AllTaskList_adapter();

  public static AllTaskList_adapter getInstance()
  {
    return UserInstance;
  }

  private AllTaskList_adapter()
  {
  }

  public static void initAllTaskList()
  {
    UserInstance = new AllTaskList_adapter();
  }
}
