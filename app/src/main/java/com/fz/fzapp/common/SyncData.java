package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.popup.OtherOption;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.SyncService;
import com.fz.fzapp.utils.Preference;

public class SyncData extends AppCompatActivity
{
  @BindView(R.id.ivOtherMenu)
  ImageView ivOtherMenu;
  @BindView(R.id.tvMsg)
  TextView tvMsg;
  @BindView(R.id.tvNotifeSync)
  TextView tvNotifeSync;
  @BindView(R.id.btnCancelGo)
  Button btnCancelGo;
  @BindView(R.id.ivGo)
  ImageView ivGo;
  @BindView(R.id.tvUserNameSync)
  TextView tvUserNameSync;
  @BindView(R.id.tvTruckSync)
  TextView tvTruckSync;


  private Activity activity = this;
  static String TAG = "[SyncData]";
  private Context context = this;
  private int firstCount = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.syncdata_lay);
    ButterKnife.bind(this);
    tvUserNameSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefFullName));
    tvTruckSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefTruckName));
    AllFunction.storeToSharedPrefCount(context, "countingArray", firstCount);
  }

  @Override
  public void onBackPressed()
  {
    moveTaskToBack(true);
  }

  @OnClick({R.id.ivOtherMenu, R.id.ivGo})
  public void onViewClicked(View view)
  {
    switch(view.getId())
    {
      case R.id.ivGo:
        tvMsg.setText(context.getResources().getString(R.string.strWait));
        btnCancelGo.setVisibility(View.VISIBLE);
        ivGo.setImageResource(R.drawable.buttongo);
        tvNotifeSync.setVisibility(view.INVISIBLE);
        AllTaskList_adapter.initAllTaskList();
        SyncService syncData = new SyncService(activity, context);
        syncData.getTaskList();
      break;
      case R.id.ivOtherMenu:
        OtherOption otherOption = new OtherOption(context, activity, Username.class);
        otherOption.ShowOtherOptions();
      break;
    }
  }
}
