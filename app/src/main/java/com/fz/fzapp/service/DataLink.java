/*
 * Copyright (c) 2016 oleh Agustinus Ignat Deswanto
 *
 *  Dilarang menyalah gunakan aplikasi ini terutama untuk tindak kejahatan.
 *  Jika ada pertanyaan seputar aplikasi ini silakan menghubungi :
 *
 *  Agustinus Ignat Deswanto
 *  Permata Depok Nilam F5a No. 5
 *  Citayam - Depok 16430
 *  Jawa Barat - Indonesia
 *  HP/WA : 085770706777
 *  Email : aignatd@gmail.com
 *
 */

package com.fz.fzapp.service;

import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.pojo.LogoutPojo;
import com.fz.fzapp.pojo.OrderPojo;
import com.fz.fzapp.pojo.ReasonPojo;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.pojo.mobileMenuPojo;
import com.fz.fzapp.sending.JobEntryHolder;
import com.fz.fzapp.sending.LogoutHolder;
import com.fz.fzapp.sending.ReasonHolder;
import com.fz.fzapp.sending.SyncTrxHolder;
import com.fz.fzapp.sending.TaskListHolder;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.sending.UserHolder;
import com.fz.fzapp.utils.FixValue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Dibuat oleh : ignat
 * Tanggal : 26-Nov-16
 * HP/WA : 0857 7070 6 777
 */
public interface DataLink
{
  @POST(FixValue.RestfulLogin)
  Call<LoginPojo> LoginService(@Body UserHolder userHolder);

  @POST(FixValue.RestfulChangePassword)
  Call<LoginPojo> ChangePasswordService(@Body UserHolder userHolder);

  @POST(FixValue.RestfulRegistration)
  Call<LoginPojo> RegistrationService(@Body UserHolder userHolder);

  @POST(FixValue.RestfulTasklist)
  Call<TaskListPojo> TaskListService(@Body TaskListHolder taskListHolder);

  @POST(FixValue.RestfulReasonlist)
  Call<ReasonPojo> ReasonListService(@Body ReasonHolder reasonHolder);

  @POST(FixValue.RestfTrackUpload)
  Call<LoginPojo> SyncTrxService(@Body SyncTrxHolder syncTrxHolder);

  @POST(FixValue.RestfulUpload)
  Call<UploadPojo> uploadService(@Body UploadHolder uploadHolder);

  @POST(FixValue.RestfulLogout)
  Call<LogoutPojo> LogoutService(@Body LogoutHolder logoutHolder);

  @POST(FixValue.RestfulLogout)
  Call<LogoutPojo> TrackingData(@Body LogoutHolder logoutHolder);

  @GET("users/menu/{id}")
  Call<mobileMenuPojo> mainMenuKrani(@Path("id") String menuid);

  @GET(FixValue.RestfulSpinnerjobEntry)
  Call<OrderPojo> listSpinnerJobOrder();

  @POST(FixValue.RestfulJobEntry)
  Call<LoginPojo> uploadJobEntry(@Body JobEntryHolder jobEntryHolder);
}

