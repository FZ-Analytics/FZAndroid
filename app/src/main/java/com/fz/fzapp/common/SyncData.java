package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.data.LogoutData;
import com.fz.fzapp.pojo.LogoutPojo;
import com.fz.fzapp.popup.OtherOption;
import com.fz.fzapp.sending.LogoutHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.SyncService;
import com.fz.fzapp.service.UploadData;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SyncData extends AppCompatActivity {
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
    private PopupMessege popupMessege = new PopupMessege();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syncdata_lay);
        ButterKnife.bind(this);
        tvUserNameSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefFullName));
        tvTruckSync.setText(AllFunction.getStringFromSharedPref(context, Preference.prefTruckName));
        AllFunction.storeToSharedPrefCount(context, "countingArray", firstCount);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick({R.id.ivOtherMenu, R.id.ivGo,R.id.btnCancelGo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivGo:
                tvMsg.setText(context.getResources().getString(R.string.strWait));
                btnCancelGo.setVisibility(View.VISIBLE);
                ivGo.setImageResource(R.drawable.buttongo);
                tvNotifeSync.setVisibility(view.INVISIBLE);
                AllTaskList_adapter.initAllTaskList();
                SyncService syncData = new SyncService(activity, context);
                syncData.getTaskList();
                break;
            case R.id.btnCancelGo:
                RetriveContent();
                break;
//      case R.id.btnEndDay:
//        OtherOption otherOption = new OtherOption(context, activity, Username.class);
//        otherOption.ShowOtherOptions();
//        break;
        }
    }

    private void RetriveContent() {
        if (CheckConnection() == -1) return;
        LogoutData logoutData = new LogoutData();
        logoutData.setUserID(AllFunction.getIntFromSharedPref(context, Preference.prefUserID));
        LogoutHolder logoutHolder = new LogoutHolder(logoutData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<LogoutPojo> ReceiveLogout = dataLink.LogoutService(logoutHolder);

        ReceiveLogout.enqueue(new Callback<LogoutPojo>() {
            @Override
            public void onResponse(Call<LogoutPojo> call, Response<LogoutPojo> response) {
                if (response.body().getCoreResponse().getCode() != FixValue.intSuccess)
                    ProccessWait(response.body().getCoreResponse().getMsg());
                else if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                    Intent intent = new Intent(SyncData.this, Username.class);
                    startActivity(intent);
                   }

            }

            @Override
            public void onFailure(Call<LogoutPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerData));
            }
        });
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            setAllOff(context.getResources().getString(R.string.msgServerResponse));
            return -1;
        }

        return 0;
    }

    private void setAllOff(String strMsg) {
        tvMsg.setText(context.getResources().getString(R.string.titleKlik));
        ivGo.setBackgroundResource(R.drawable.buttonthick);
        popupMessege.ShowMessege1(context, strMsg);
    }

    private void ProccessWait(String msg) {
        tvNotifeSync.setVisibility(View.VISIBLE);
        tvNotifeSync.setText(msg);
        ivGo.setImageResource(R.drawable.buttonthick);
    }

}
