package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.fz.fzapp.model.UserResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class LoginPojo
{
  @SerializedName("CoreResponse")
  @Expose
  private CoreResponse coreResponse;
  @SerializedName("UserResponse")
  @Expose
  private UserResponse userResponse;

  public CoreResponse getCoreResponse()
  {
    return coreResponse;
  }

  public void setCoreResponse(CoreResponse coreResponse)
  {
    this.coreResponse = coreResponse;
  }

  public UserResponse getUserResponse()
  {
    return userResponse;
  }

  public void setUserResponse(UserResponse userResponse)
  {
    this.userResponse = userResponse;
  }
}
