package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.Database_adapter;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Password extends AppCompatActivity {
    @BindView(R.id.etPasswordLogin)
    EditText etPasswordLogin;

    private Activity activity = this;
    private PopupMessege popupMessege = new PopupMessege();
    static ProgressDialog progressDialog;

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
                lstInput.clear();
                lstMsg.clear();
                lstInput.add(etPasswordLogin);
                lstMsg.add(getResources().getString(R.string.msgInputPassword));

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
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getUserID(), Preference.prefUserID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getName(), Preference.prefFullName);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getLnkRoleID(), Preference.prefRoleID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getVehicleName(), Preference.prefTruckName);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getVehicleID(), Preference.prefVehicleID);
                        AllFunction.storeToSharedPref(context, response.body().getUserResponse().getTimeTrackLocation(), Preference.prefVTimeTrackLocation);


//            AllFunction.storeToSharedPref(context, response.body().getUserResponse().getTimeTrackLocation(), Preference.prefTimeTrack);

                        Intent PasswordIntent = new Intent(Password.this, SyncData.class);
                        startActivity(PasswordIntent);
                        finish();
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
}
