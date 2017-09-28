package com.fz.fzapp.popup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.MenuItem;
import android.view.View;
import com.fz.fzapp.R;
import com.fz.fzapp.data.User;
import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.sending.UserHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Agustinus Ignat on 31-Aug-17.
 */
public class OtherOption
{
  private Context context;
  private View OtherOptions;
  private Activity activity;
  private Class<?> cls;

  public OtherOption(Context context, Activity activity, Class<?> cls)
  {
    this.context = context;
//    OtherOptions = otherOptions;
    this.activity = activity;
    this.cls = cls;
  }

  public void ShowOtherOptions()
  {
    MenuBuilder menuBuilder = new MenuBuilder(context);
    new SupportMenuInflater(context).inflate(R.menu.othermenu, menuBuilder);

    final MenuPopupHelper menuHelper = new MenuPopupHelper(context, menuBuilder, OtherOptions);
    menuHelper.setForceShowIcon(true);
    menuHelper.show();

    menuBuilder.setCallback(new MenuBuilder.Callback()
    {
      @Override
      public boolean onMenuItemSelected(MenuBuilder menu, MenuItem menuItem)
      {
        menuHelper.dismiss();

        switch(menuItem.getItemId())
        {
          case R.id.mnuUpload:
            // TodoSyncTaskList();
            return true;
          case R.id.mnuChangePassword:
            ChangePassword cdMenuPass = new ChangePassword(context, activity);
            cdMenuPass.show();
            return true;
          case R.id.mnuLogout:
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
              .setTitle(R.string.titleMessege)
              .setMessage(context.getResources().getString(R.string.strLogoutApps))
              .setIcon(android.R.drawable.ic_dialog_alert)
              .setCancelable(false)
              .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface dialog, int which)
                {
                  LogoutUser();
                }
              })
              .setNegativeButton(R.string.strBtnCancel, new DialogInterface.OnClickListener()
              {
                public void onClick(DialogInterface dialog, int id)
                {
                  dialog.cancel();
                }
              });

            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return false;
      }

      @Override
      public void onMenuModeChange(MenuBuilder menu)
      {
      }
    });
  }

  private void LogoutUser()
  {
    final PopupMessege popupMessege = new PopupMessege();
    final ProgressDialog progressDialog;

    progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.strWait), context.getResources().getString(R.string.strTaskListReload));
    progressDialog.setCancelable(false);

    if(AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE)
    {
      progressDialog.dismiss();
      popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerResponse));
      return;
    }

    User.initUser();
    User.getInstance().setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));

    UserHolder loginHolder = new UserHolder(User.getInstance());
    DataLink dataLink = AllFunction.BindingData();

    final Call<LoginPojo> ReceivePojo = dataLink.LogoutService(loginHolder);

    ReceivePojo.enqueue(new Callback<LoginPojo>()
    {
      @Override
      public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response)
      {
        progressDialog.dismiss();

        if(response.isSuccessful())
        {
          if(response.body().getCoreResponse().getCode() == FixValue.intFail)
            popupMessege.ShowMessege1(context, response.body().getCoreResponse().getMsg());
          else
          {
            Intent SinkronIntent = new Intent(context, cls);
            context.startActivity(SinkronIntent);
            activity.finish();
          }
        }else
          popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerData));
      }

      @Override
      public void onFailure(Call<LoginPojo> call, Throwable t)
      {
        progressDialog.dismiss();
        popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerFailure));
      }
    });
  }
}
