package com.fz.fzapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.fz.fzapp.R;
import com.fz.fzapp.adapter.AllTaskList_adapter;
import com.fz.fzapp.adapter.Database_adapter;
import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.TrackingTrx;
//import com.fz.fzapp.data.UploadRespone;
import com.fz.fzapp.pojo.TaskListPojo;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.service.UploadData;
import com.fz.fzapp.utils.DatabaseHandler;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.UploadDataToServer;
import com.fz.fzapp.utils.uploadData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Duty extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    @BindView(R.id.btnDutyFails)
    Button btnDutyFails;
    @BindView(R.id.btnDutyDone)
    Button btnRecapDone;
    @BindView(R.id.tvTrackTest)
    TextView tvTrackTest;
    @BindView(R.id.tvStartDutty)
    TextView tvStartDutty;
    @BindView(R.id.tvTimeActualDutty)
    TextView tvTimeActualDutty;
    @BindView(R.id.tvDestinationDutty)
    TextView tvDestinationDutty;
    @BindView(R.id.tvtimeEstimateDutty)
    TextView tvtimeEstimateDutty;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.ivGo)
    ImageView ivGo;
    @BindView(R.id.borderLayout1)
    LinearLayout borderLayout1;
    @BindView(R.id.borderLayout2)
    LinearLayout borderLayout2;
    @BindView(R.id.splitter)
    View splitter;
    @BindView(R.id.dutyDivisi)
    TextView dutyDivisi;
    @BindView(R.id.dutyBlock)
    TextView dutyBlock;
    @BindView(R.id.DutyLokasi)
    TextView DutyLokasi;
    @BindView(R.id.dutyEmptyBinLocation)
    TextView dutyEmptyBinLocation;
    @BindView(R.id.llViewDetails)
    LinearLayout llViewDetails;

    private PopupMessege popupMessege =new PopupMessege();

    private Activity activity = this;
    static String TAG = "[DutyData]";
    private Context context = this;
    private SimpleDateFormat tf, df;
    private Calendar calendar;
    private String strEstTime;
    private Integer onDuty = 3;
    private Integer onActivityJump = 1;
    ArrayList<uploadData> data;
    private int lastActivity;
    private Database_adapter database_adapter;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CountDownTimerToTrackLocation TimerToTrackLocation;
    long diff = 0;
    private long startTimes = 0L;
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    int countingArray;
    int getReasonId;
    private Handler myHandler = new Handler();
    SimpleDateFormat form;
    private TaskListPojo taskListPojo = new TaskListPojo();
    @BindView(R.id.tvDivision)
    TextView tvDivision;
    UploadDataToServer uploadDataToServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_lay);
        lastActivity = 2;
        AllFunction.storeToSharedPref(context, lastActivity, Preference.prefLastActivity);
        ButterKnife.bind(this);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        form = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.US);

        taskListPojo = AllFunction.getObjectFromSharedPrefObj(context, TaskListPojo.class, Preference.prefAllTaskList);
        database_adapter = new Database_adapter(context);
        countingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
//        GetLocation();
        if (AllFunction.CheckPermission(Duty.this, this)) {
        }
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        long TimeTrackLocation = AllFunction.getIntFromSharedPref(context, Preference.prefVTimeTrackLocation);
//        long TimeTrackLocation = 10000;
        Log.d("waktu", String.valueOf(TimeTrackLocation));
        TimerToTrackLocation = new CountDownTimerToTrackLocation(TimeTrackLocation, 1000);
        TimerToTrackLocation.start();
        File fScr = new File("/data/data/com.fz.fzapp/databases/", "tasklist.sqlite");

        if (fScr.exists()) {
            Toast.makeText(context, "File exists", Toast.LENGTH_LONG);
            File fDest = new File(Environment.getExternalStorageDirectory(), "tasklist.sqlite");

            try {
                copyToExternal(fScr, fDest);
            } catch (IOException e) {
                // e.printStackTrace()
            }
        }
        RetriveContent();

    }

    private void RetriveContent() {

        calendar = Calendar.getInstance();
        String timeDutty = null;

        int CountingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
        timeDutty = tf.format(calendar.getTime());
        AllFunction.storeToSharedPref(context, timeDutty, Preference.prefGetTime);
        String timeEstimateDutty = taskListPojo.getTaskListResponse().get(CountingArray).getEnd();
        String StartDutty = taskListPojo.getTaskListResponse().get(CountingArray).getFrom();
        String DestinationDutty = taskListPojo.getTaskListResponse().get(CountingArray).getTo();
        String timeEnd = AllFunction.getTime(timeEstimateDutty);
        AllFunction.storeToSharedPref(context, AllFunction.curentTime(), Preference.prefActualStart);

        try {
            Date startTime = form.parse(form.format(calendar.getTime()));
            Date endTime = form.parse(timeEstimateDutty);
            diff = endTime.getTime() - startTime.getTime();
            if (diff < 0) {
                countingUpTimer(diff);
            } else {
                countingDownTimer(diff);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        tvDivision.setText(AllFunction.getStringFromSharedPref(context, Preference.prefTruckName));
        tvStartDutty.setText(StartDutty);
        tvDestinationDutty.setText(DestinationDutty);
        tvTimeActualDutty.setText(AllFunction.getStringFromSharedPref(context, Preference.prefGetTime));
        tvtimeEstimateDutty.setText(timeEnd);
        dutyDivisi.setText(taskListPojo.getTaskListResponse().get(CountingArray).getDivID());
        dutyBlock.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBlocks());
        DutyLokasi.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBinLocation());
        dutyEmptyBinLocation.setText(taskListPojo.getTaskListResponse().get(CountingArray).getBinEmpty());
    }

    private void countingDownTimer(long diff) {
        final long finalDiff = diff;
        new CountDownTimer(finalDiff, 1000) { // adjust the milli seconds here
            public void onTick(long diff) {
                tvTimer.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(diff),
                        TimeUnit.MILLISECONDS.toMinutes(diff) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                        TimeUnit.MILLISECONDS.toSeconds(diff) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff))));
            }

            public void onFinish() {
                countingUpTimer(0);
            }
        }.start();
    }

    private void countingUpTimer(long absDif) {
        onDuty = 2;
        diff = absDif;
        ivGo.setImageResource(R.drawable.circle_red);
        borderLayout1.setBackgroundResource(R.drawable.border_red);
        borderLayout2.setBackgroundResource(R.drawable.border_red);
        llViewDetails.setBackgroundResource(R.drawable.border_red);
        tvTimer.setTextColor(context.getResources().getColor(R.color.red));
        splitter.setBackgroundResource(R.color.red);
        startTimes = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTimes - diff;
            finalTime = timeSwap + timeInMillies;
            tvTimer.setText(String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(finalTime),
                    TimeUnit.MILLISECONDS.toMinutes(finalTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(finalTime)),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime))));
            myHandler.postDelayed(this, 0);
        }

    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @OnClick({R.id.btnDutyDone, R.id.btnDutyFails})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDutyFails:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle(R.string.titleMessege)
                        .setMessage(R.string.PopupMessage)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onClickBtnDutyFails();
                            }
                        })
                        .setNegativeButton(R.string.strBtnCancel2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.btnDutyDone:
                onClickBtnDutyDone();
//
//                AlertDialog.Builder builders = new AlertDialog.Builder(context);
//                builders
//                        .setTitle(R.string.titleMessege)
//                        .setMessage(R.string.PopupMessage)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.strBtnOK, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                onClickBtnDutyDone();
//                            }
//                        })
//                        .setNegativeButton(R.string.strBtnCancel2, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//                AlertDialog alerts = builders.create();
//                alerts.show();
                break;
        }
    }

    private void onClickBtnDutyFails() {
        TimerToTrackLocation.cancel();
        getReasonId = 1;
        //UploadFinishJob();
        AllFunction.storeToSharedPref(context, getReasonId, "getReasonId");
        //Keluar dari job
        AllFunction.storeToSharedPref(context, AllFunction.curentTime(), Preference.prefActualEnd);
        onDuty = 1;
        Intent ReasonIntent = new Intent(Duty.this, Reason.class);
        ReasonIntent.putExtra("onDuty", onDuty);
        ReasonIntent.putExtra("onActivityJump", onActivityJump);
        startActivity(ReasonIntent);
        finish();
    }

    private void onClickBtnDutyDone() {
        AllFunction.storeToSharedPref(context, AllFunction.curentTime(), Preference.prefActualEnd);
        if (onDuty != 2) {
            getReasonId = 0;
            //Late
            if (taskListPojo.getTaskListResponse().size() == countingArray + 1) {
                AllFunction.storeToSharedPref(context, getReasonId, "getReasonId");
                //Success and one job
                AllFunction.uploadSync(0, context, 0);
                TimerToTrackLocation.cancel();
                CheckMetodeUpload();
                popupMessege.ShowMessegeUpload(context,activity);

//                UploadFinishJob();


            } else {
                //Succses and half job
                AllFunction.uploadSync(0, context, 0);
                AllFunction.storeToSharedPrefCount(context, "countingArray", countingArray);
                Intent planningIntent = new Intent(Duty.this, Planning.class);
                startActivity(planningIntent);
                TimerToTrackLocation.cancel();
                finish();
            }
        } else {
            //Late
            getReasonId = 2;
            AllFunction.storeToSharedPref(context, getReasonId, "getReasonId");
            AllFunction.getIntFromSharedPref(context, Preference.prefJobID);
            Intent ReasonIntents = new Intent(Duty.this, Reason.class);
            ReasonIntents.putExtra("onActivityJump", onActivityJump);
            ReasonIntents.putExtra("onDuty", onDuty);
            startActivity(ReasonIntents);
            TimerToTrackLocation.cancel();
            finish();
        }
    }

    private void CheckMetodeUpload() {
        signalStreght();
    }

    private void signalStreght() {
    }


    public void UploadFinishJob() {
        final Gson gson = new Gson();
        UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance().getUploadData(),9000);
        Log.d("Test", "getUploadHolder: " + gson.toJson(uploadHolder));

        if (CheckConnection() == -1) return;
        DataLink dataLink = AllFunction.BindingData();
        final Call<UploadPojo> ReceiveUpload = dataLink.uploadService(uploadHolder);
        ReceiveUpload.enqueue(new Callback<UploadPojo>() {
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


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1000).show();
            } else {
                Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.strDeviceNotSupported),
                        Toast.LENGTH_LONG).show();
                finish();
            }

            return false;
        }

        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private class CountDownTimerToTrackLocation extends CountDownTimer {
        public CountDownTimerToTrackLocation(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long l) {
            // Log.d(TAG, "seconds remaining -> " + l / 1000);
        }

        public void onFinish() {
            TimerToTrackLocation.cancel();

            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } catch (SecurityException e) {
                Log.d(TAG, "onFinish:");
            }

            if (mLastLocation != null)
                SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()),
                        null, null, null);

            TimerToTrackLocation.start();

//            if (mLastLocation != null) {
////                Integer intConnect = AllFunction.isNetworkAvailable(context);
//                SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()),
//                        null, null, null);
//            }
        }

    }

    private void SaveTrackToSQLite(String Latitude, String Longitude, String Date, String Time, Integer intConnect) {
        String strEndTime = AllFunction.curentTime();
        int userId = AllFunction.getIntFromSharedPref(context, Preference.prefUserID);
        int vehicleId = AllFunction.getIntFromSharedPref(context, Preference.prefUserID);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();
//        Toast.makeText(context, Latitude + " * " + Longitude + " * " + userId + " * " + vehicleId + " * " + strEndTime, Toast.LENGTH_LONG).show();
        Log.d("Woi", Latitude + " * " + Longitude + " * " + userId + " * " + vehicleId + " * " + strEndTime);
        Log.d("test", "Heruuuuuuuuuuuuuu");

        hashMap.put("Latitude", Latitude);
        hashMap.put("Longitude", Longitude);
        hashMap.put("EndDate", strEndTime);
        hashMap.put("UserID", String.valueOf(userId));
        hashMap.put("VehicleID", String.valueOf(vehicleId));
        hashMap.put("Status", String.valueOf(0));

        if (intConnect != null)
            hashMap.put("Connection", String.valueOf(intConnect));

        database_adapter.SaveTrackingData(hashMap);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        getCurrentLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
//        TimerToTrackLocation.cancel();
//        SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()),
//                null, null, null);
//        TimerToTrackLocation.start();
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(),
                    context.getResources().getString(R.string.strStartLocation), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                context.getResources().getString(R.string.strConnectionFailed), Toast.LENGTH_LONG)
                .show();
    }


    private boolean getCurrentLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.strCurrentLocation),
                    Toast.LENGTH_LONG).show();

            return false;
        }

        if (mLastLocation == null) return false;

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            e.printStackTrace();
            Toast.makeText(context, getResources().getString(R.string.strDatabaseFailed), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void copyToExternal(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}