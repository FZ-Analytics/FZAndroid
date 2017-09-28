package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 24-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class CoreResponse
{
  @SerializedName("Code")
  @Expose
  private int code;
  @SerializedName("Msg")
  @Expose
  private String msg;

  public int getCode()
  {
    return code;
  }

  public void setCode(int code)
  {
    this.code = code;
  }

  public String getMsg()
  {
    return msg;
  }

  public void setMsg(String msg)
  {
    this.msg = msg;
  }
}
