package com.fz.fzapp.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fz.fzapp.R;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.User;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.Preference;

public class Splash extends AppCompatActivity {
    private TextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_lay);

        tvAppVersion = (TextView) findViewById(R.id.tvVersion);
        String myVersionName = "Unknown";
        final Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // e.printStackTrace();
        }

        tvAppVersion.setText("Version " + myVersionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(FixValue.strPreferenceName, null);
                if (AllFunction.getIntFromSharedPref(context, Preference.prefCheckLogin) == 0) {

                    User.initUser();
                    Intent mainIntent = new Intent(Splash.this, Username.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    //initiaion
                    AllUploadData.initUser();
                    if (AllFunction.getIntFromSharedPref(context, Preference.prefLastActivity) == 0) {
                        Intent mainIntent = new Intent(Splash.this, SyncData.class);
                        startActivity(mainIntent);
                        finish();
                    } else if (AllFunction.getIntFromSharedPref(context, Preference.prefLastActivity) == 1) {
                        Intent mainIntent = new Intent(Splash.this, Planning.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(Splash.this, Duty.class);
                        startActivity(mainIntent);
                        finish();
                    }
                }

            }
        }, FixValue.SPLASH_DISPLAY_LENGHT);
    }
}
