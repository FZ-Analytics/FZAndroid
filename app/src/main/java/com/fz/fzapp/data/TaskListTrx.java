package com.fz.fzapp.data;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskListTrx
{
  private Integer ReportID;
  private Integer lnkUserID;
  private Integer lnkTaskID;
  private Integer lnkReasonID;
  private Integer lnkLocationID;
  private String Notes;
  private Integer Status;

  public Integer getReportID()
  {
    return ReportID;
  }

  public void setReportID(Integer reportID)
  {
    ReportID = reportID;
  }

  public Integer getLnkUserID()
  {
    return lnkUserID;
  }

  public void setLnkUserID(Integer lnkUserID)
  {
    this.lnkUserID = lnkUserID;
  }

  public Integer getLnkTaskID()
  {
    return lnkTaskID;
  }

  public void setLnkTaskID(Integer lnkTaskID)
  {
    this.lnkTaskID = lnkTaskID;
  }

  public Integer getLnkReasonID()
  {
    return lnkReasonID;
  }

  public void setLnkReasonID(Integer lnkReasonID)
  {
    this.lnkReasonID = lnkReasonID;
  }

  public Integer getLnkLocationID()
  {
    return lnkLocationID;
  }

  public void setLnkLocationID(Integer lnkLocationID)
  {
    this.lnkLocationID = lnkLocationID;
  }

  public String getNotes()
  {
    return Notes;
  }

  public void setNotes(String notes)
  {
    Notes = notes;
  }

  public Integer getStatus()
  {
    return Status;
  }

  public void setStatus(Integer status)
  {
    Status = status;
  }
}
