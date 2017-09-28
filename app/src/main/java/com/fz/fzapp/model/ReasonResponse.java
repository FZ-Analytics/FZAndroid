package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 24-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class ReasonResponse
{
  @SerializedName("ReasonID")
  @Expose
  private int reasonid;
  @SerializedName("ReasonName")
  @Expose
  private String reasonname;
  @SerializedName("ReasonDesc")
  @Expose
  private String reasondesc;

  public int getReasonid()
  {
    return reasonid;
  }

  public void setReasonid(int reasonid)
  {
    this.reasonid = reasonid;
  }

  public String getReasonname()
  {
    return reasonname;
  }

  public void setReasonname(String reasonname)
  {
    this.reasonname = reasonname;
  }

  public String getReasondesc()
  {
    return reasondesc;
  }

  public void setReasondesc(String reasondesc)
  {
    this.reasondesc = reasondesc;
  }
}
