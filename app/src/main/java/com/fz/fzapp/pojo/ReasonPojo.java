package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.fz.fzapp.model.ReasonResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class ReasonPojo
{
  @SerializedName("CoreResponse")
  @Expose
  private CoreResponse coreResponse;
  @SerializedName("ReasonResponse")
  @Expose
  private List<ReasonResponse> reasonResponse;

  public CoreResponse getCoreResponse()
  {
    return coreResponse;
  }

  public void setCoreResponse(CoreResponse coreResponse)
  {
    this.coreResponse = coreResponse;
  }

  public List<ReasonResponse> getReasonResponse()
  {
    return reasonResponse;
  }

  public void setReasonResponse(List<ReasonResponse> reasonResponse)
  {
    this.reasonResponse = reasonResponse;
  }
}
