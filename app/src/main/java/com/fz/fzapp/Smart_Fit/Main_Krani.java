package com.fz.fzapp.Smart_Fit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.adapter_main_smart_fit;
import com.fz.fzapp.pojo.mobileMenuPojo;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Heru Permana on 11/28/2017.
 */

public class Main_Krani extends Activity {
    private Context context = this;
    @BindView(R.id.rvMenu_smart_fit)
    RecyclerView rvSmartfit;
    Activity activity = this;
    adapter_main_smart_fit adapter_main_smart_fit = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_order);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvSmartfit.setLayoutManager(layoutManager);
        retriveContent();
    }

    private void retriveContent() {
        String RoleID = String.valueOf(AllFunction.getIntFromSharedPref(context, Preference.prefRoleID));
        if (CheckConnection() == -1) return;
        DataLink dataLink = AllFunction.BindingData();
        final Call<mobileMenuPojo> mobileMenuPojo = dataLink.mainMenuKrani(RoleID);
        mobileMenuPojo.enqueue(new Callback<mobileMenuPojo>() {

            @Override
            public void onResponse(Call<mobileMenuPojo> call, Response<mobileMenuPojo> response) {
                rvSmartfit.invalidate();
                final Gson gson = new Gson();
                String gsonToString = gson.toJson(response.body());
                adapter_main_smart_fit = new adapter_main_smart_fit(activity, context, response.body().getMobileMenuRsp(),gsonToString);
                rvSmartfit.setAdapter(adapter_main_smart_fit);
            }

            @Override
            public void onFailure(Call<mobileMenuPojo> call, Throwable t) {

            }
        });
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
            return -1;
        }
        return 0;
    }
}
