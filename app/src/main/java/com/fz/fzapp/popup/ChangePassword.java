package com.fz.fzapp.popup;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.data.User;
import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.sending.UserHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 08-Dec-16
 * HP/WA : 0857 7070 6 777
 */
public class ChangePassword extends Dialog
{
  @BindView(R.id.etOldPassword)
  EditText etOldPassword;
  @BindView(R.id.etNewPassword)
  EditText etNewPassword;
  @BindView(R.id.etVerificationPassword)
  EditText etVerificationPassword;

  private String TAG = "[ChangePassword]";
  private Context context;

  private PopupMessege popupMessege = new PopupMessege();
  private ProgressDialog progressDialog;
  List<EditText> lstInput = new ArrayList<>();
  List<String> lstMsg = new ArrayList<>();
  private List<String> lstLogin = new ArrayList<>();

  public Activity ParentAct;

  public ChangePassword(Context mContext, Activity parentAct)
  {
    super(parentAct);
    this.ParentAct = parentAct;
    this.context = mContext;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.changepassword_lay);
    ButterKnife.bind(this);
  }

  @OnClick({R.id.btnChangePassword, R.id.btnCancelPassword})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.btnChangePassword:
        lstInput.clear();
        lstMsg.clear();
        lstInput.add(etOldPassword);
        lstInput.add(etNewPassword);
        lstInput.add(etVerificationPassword);
        lstMsg.add(ParentAct.getResources().getString(R.string.strOldPasswordBlank));
        lstMsg.add(ParentAct.getResources().getString(R.string.strNewPasswordBlank));
        lstMsg.add(ParentAct.getResources().getString(R.string.strVerificationBlank));

        if(AllFunction.InputCheck(lstInput, lstMsg, getContext()))
        {
          if(!etNewPassword.getText().toString().matches(etVerificationPassword.getText().toString()))
          {
            popupMessege.ShowMessege1(getContext(), ParentAct.getResources().getString(R.string.strPasswordVerification));
            etNewPassword.requestFocus();
          }
          else if(etOldPassword.getText().toString().matches(etNewPassword.getText().toString()))
          {
            popupMessege.ShowMessege1(getContext(), ParentAct.getResources().getString(R.string.strPasswordSame));
            etNewPassword.requestFocus();
          }
          else
            ChangePasswordProcess();
        }
      break;
      case R.id.btnCancelPassword:
        dismiss();
      break;
    }
  }

  private void ChangePasswordProcess()
  {
    progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.strWait),
                                         context.getResources().getString(R.string.strSyncProgress));
    progressDialog.setCancelable(false);
    progressDialog.show();

    if(AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE)
    {
      progressDialog.dismiss();
      popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerResponse));
      return;
    }

    User.initUser();
    User.getInstance().setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
    User.getInstance().setPassword(new String(Hex.encodeHex(DigestUtils.md5(etOldPassword.getText().toString().trim()))));
    User.getInstance().setNewPassword(new String(Hex.encodeHex(DigestUtils.md5(etNewPassword.getText().toString().trim()))));

    UserHolder loginHolder = new UserHolder(User.getInstance());
    DataLink dataLink = AllFunction.BindingData();

    final Call<LoginPojo> ReceivePojo = dataLink.ChangePasswordService(loginHolder);

    ReceivePojo.enqueue(new Callback<LoginPojo>()
    {
      @Override
      public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response)
      {
        progressDialog.dismiss();

        if (response.isSuccessful())
        {
          if(response.body().getCoreResponse().getCode() == FixValue.intFail)
            popupMessege.ShowMessege1(context, response.body().getCoreResponse().getMsg());
          else
            dismiss();
        }
        else
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

