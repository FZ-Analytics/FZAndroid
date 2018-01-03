package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.Smart_Fit.Main_Krani;
import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllReason_adapter;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.data.ReasonData;
import com.fz.fzapp.data.User;
import com.fz.fzapp.model.ReasonResponse;
import com.fz.fzapp.pojo.LoginPojo;
import com.fz.fzapp.pojo.ReasonPojo;
import com.fz.fzapp.sending.ReasonHolder;
import com.fz.fzapp.sending.UserHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Password extends AppCompatActivity {
    @BindView(R.id.etPasswordLogin)
    EditText etPasswordLogin;

    private Activity activity = this;
    private PopupMessege popupMessege = new PopupMessege();
    static ProgressDialog progressDialog;
    private int loginFlag = 0;
    static String TAG = "[Password]";
    private Context context = this;
    List<EditText> lstInput = new ArrayList<>();
    List<String> lstMsg = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_lay);
        ButterKnife.bind(this);
        File file = getApplicationContext().getDatabasePath(Database_adapter.databasename);

        if (!file.exists()) {
            Database_adapter DataBaseTasklist = new Database_adapter(this);
            DataBaseTasklist.getReadableDatabase();

            if (!copyDatabase(this)) return;
        }
    }

    @OnClick({R.id.rlPasswordLogin, R.id.btnPasswordLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlPasswordLogin:
            case R.id.btnPasswordLogin:
//                LayoutInflater li = LayoutInflater.from(context);
//                View promptsView = li.inflate(R.layout.tonase_popup, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                        context);
//
//                // set prompts.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                final EditText userInput = (EditText) promptsView
//                        .findViewById(R.id.editTextDialogUserInput);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        // get user input and set it to result
//                                        // edit text
////                                        result.setText(userInput.getText());
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });f
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();
                lstInput.clear();
                lstMsg.clear();
                lstInput.add(etPasswordLogin);
                lstMsg.add(getResources().getString(R.string.msgInputPassword));
                AllReason_adapter.initAllTaskList();
                if (AllFunction.InputCheck(lstInput, lstMsg, context)) {
                    User.getInstance().setPassword(new String(Hex.encodeHex(DigestUtils.md5(etPasswordLogin.getText().toString().trim()))));
                    ProceedLogin();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent PasswordIntent = new Intent(Password.this, Username.class);
        startActivity(PasswordIntent);
        finish();
    }

    private void ProceedLogin() {
        progressDialog = ProgressDialog.show(context, context.getResources().getString(R.string.strWait), context.getResources().getString(R.string.strLoginProcess));
        progressDialog.setCancelable(false);
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            progressDialog.dismiss();
            popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerResponse));
            return;
        }

        UserHolder userHolder = new UserHolder(User.getInstance());
        DataLink dataLink = AllFunction.BindingData();

        final Call<LoginPojo> ReceivePojo = dataLink.LoginService(userHolder);

        ReceivePojo.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    Log.d("TaskList", response.body().getCoreResponse().getMsg() + response.body().getCoreResponse().getCode());
                    if (response.body().getCoreResponse().getCode() == FixValue.intFail)
                        popupMessege.ShowMessege1(context, response.body().getCoreResponse().getMsg());
                    else {
                        loginFlag = 1;
                        AllFunction.storeToSharedPref(context, loginFlag, Preference.prefCheckLogin);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getUserID(), Preference.prefUserID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getName(), Preference.prefFullName);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getLnkRoleID(), Preference.prefRoleID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getVehicleName(), Preference.prefTruckName);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getVehicleID(), Preference.prefVehicleID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getTimeTrackLocation(), Preference.prefVTimeTrackLocation);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getMobileMenuID(), Preference.prefMobileMenuID);
                        ProcessReasonFail();

                    }
                } else
                    popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerData));
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                progressDialog.dismiss();
                popupMessege.ShowMessege1(context, context.getResources().getString(R.string.msgServerFailure));
            }
        });
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(Database_adapter.databasename);
            String strFile = Database_adapter.databaselocation + Database_adapter.databasename;
            OutputStream outputStream = new FileOutputStream(strFile);
            byte[] buff = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(context, getResources().getString(R.string.strDatabaseFailed), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void ProcessReasonFail() {
        if (CheckConnection() == -1) return;

        ReasonData reasonData = new ReasonData();
        reasonData.setReasonID(1);

        ReasonHolder reasonHolder = new ReasonHolder(reasonData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<ReasonPojo> ReceivePojo = dataLink.ReasonListService(reasonHolder);

        ReceivePojo.enqueue(new Callback<ReasonPojo>() {
            @Override
            public void onResponse(Call<ReasonPojo> call, Response<ReasonPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCoreResponse().getCode() == FixValue.intFail)
                        setAllOff(response.body().getCoreResponse().getMsg());
                    else {
                        List<ReasonResponse> gsonFail = response.body().getReasonResponse();
                        storeToReason(gsonFail, Preference.prefFail, 0);
                        ProcessReasonLate();
                    }
                } else
                    setAllOff(context.getResources().getString(R.string.msgServerData));
            }

            @Override
            public void onFailure(Call<ReasonPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerFailure));
            }
        });
    }


    private void ProcessReasonLate() {
        if (CheckConnection() == -1) return;

        ReasonData reasonData = new ReasonData();
        reasonData.setReasonID(2);

        ReasonHolder reasonHolder = new ReasonHolder(reasonData);
        DataLink dataLink = AllFunction.BindingData();

        final Call<ReasonPojo> ReceivePojo = dataLink.ReasonListService(reasonHolder);

        ReceivePojo.enqueue(new Callback<ReasonPojo>() {
            @Override
            public void onResponse(Call<ReasonPojo> call, Response<ReasonPojo> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCoreResponse().getCode() == FixValue.intFail)
                        setAllOff(response.body().getCoreResponse().getMsg());
                    else {
                        List<ReasonResponse> gsonLate = response.body().getReasonResponse();
                        storeToReason(gsonLate, Preference.prefLate, 1);

//                        AllFunction.storeToSharedPref(context, gson, Preference.prefFail);
//                        AllReason_adapter.getInstance().setAllresponselate(response.body().getReasonResponse());
                        IntentCondition();

                    }
                } else
                    setAllOff(context.getResources().getString(R.string.msgServerData));
            }

            @Override
            public void onFailure(Call<ReasonPojo> call, Throwable t) {
                setAllOff(context.getResources().getString(R.string.msgServerData));
            }
        });
    }

    private void IntentCondition() {
        Intent PasswordIntent;
//        if (Preference.prefRoleID.equals("1")) {
//            PasswordIntent = new Intent(Password.this, SyncData.class);
//        } else {
//            PasswordIntent = new Intent(Password.this, Main_Krani.class);
//        }
        PasswordIntent = new Intent(Password.this, SyncData.class);

        startActivity(PasswordIntent);
        finish();
    }

    private void storeToReason(List<ReasonResponse> gsonFail, String key, int mode) {
        String httpParamJSONList = new Gson().toJson(gsonFail);
        SharedPreferences prefs = activity.getSharedPreferences(key, 2);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, httpParamJSONList);
        editor.apply();
    }

    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            Toast.makeText(context, R.string.msgCheckConnection, Toast.LENGTH_LONG).show();
            return -1;
        }
        return 0;
    }

    private void setAllOff(String strMsg) {
        popupMessege.ShowMessege1(context, strMsg);
    }

}
