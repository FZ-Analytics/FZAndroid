package com.fz.fzapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.fz.fzapp.R;
import com.fz.fzapp.common.SyncData;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.UploadData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Heru Permana on 12/5/2017.
 */

public class UploadDataToServer extends Activity {
    private List<UploadPlanData> uploadPlanData;
    private int actualKg;
    private Context context;
    private Activity activity;

    public UploadDataToServer( Context context, int ActualKg, Activity activity) {
//        this.uploadPlanData = uploadHolder;
        this.context = context;
        this.actualKg = ActualKg;
        this.activity = activity;

    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
            return -1;
        }
        return 0;
    }

    public void uploadDataServer() {
        final Gson gson = new Gson();
        UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance().getUploadData(), 9000);
        Log.d("Test", "getUploadHolder: " + gson.toJson(uploadHolder));

//        if (CheckConnection == -1) return;
        DataLink dataLink = AllFunction.BindingData();
        final Call<UploadPojo> ReceiveUpload = dataLink.uploadService(uploadHolder);
        ReceiveUpload.enqueue(new Callback<UploadPojo>()

        {
            @Override
            public void onResponse(Call<UploadPojo> call, Response<UploadPojo> response) {
                if (response.body().getCoreResponse().getCode() == FixValue.intSuccess) {
                    Log.d("UploadTask", String.valueOf(response));
                    onProcessSyncData();
                } else {
                    Toast.makeText(context, response.body().getCoreResponse().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UploadPojo> call, Throwable t) {
                Toast.makeText(context, R.string.msgServerFailure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onProcessSyncData() {
        HashMap<String, String> listSyncTable = new HashMap<>();
        listSyncTable.clear();
        listSyncTable.put("tracking", "tracking");
        new UploadData(activity, context, listSyncTable).execute();
        intentToSync();
    }

    private void intentToSync() {
        Intent SyncIntent = new Intent(context, SyncData.class);
        context.startActivity(SyncIntent);
        activity.finish();
    }

}
