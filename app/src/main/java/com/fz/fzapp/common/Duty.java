package com.fz.fzapp.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.fz.fzapp.data.UploadPlanData;
//import com.fz.fzapp.data.UploadRespone;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.sending.UploadHolder;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.service.DataLink;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.fz.fzapp.utils.uploadData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.messages.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

    private PopupMessege popupMessege = new PopupMessege();
    static ProgressDialog progressDialog;

    private Activity activity = this;
    static String TAG = "[DutyData]";
    private Context context = this;
    private Integer intDutyTask;
//  private CountDownClass CountingTimer;

    private SimpleDateFormat tf, df;
    private Calendar calendar;
    private String strEstTime;
    private Integer onDuty = 3;
    private Integer onActivityJump = 1;
    ArrayList<uploadData> data;

    private Database_adapter database_adapter;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CountDownTimerToTrackLocation TimerToTrackLocation;
    private long startTimes = 0L;
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    int countingArray;
    private Handler myHandler = new Handler();
    SimpleDateFormat form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_lay);
        ButterKnife.bind(this);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        form = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        database_adapter = new Database_adapter(context);
        countingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
        parameterUpload();
        RetriveContent();
//    StartDutyCheck();
//
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        long TimeTrackLocation = AllFunction.getLongFromSharedPref(context, Preference.prefTimeTrack);
        TimerToTrackLocation = new CountDownTimerToTrackLocation(TimeTrackLocation, 1000);
        TimerToTrackLocation.cancel();
    }

    private void parameterUpload() {


    }

    private void RetriveContent() {
        calendar = Calendar.getInstance();
        String timeDutty = null;
        long diff = 0;
        int CountingArray = AllFunction.getIntFromSharedPref(context, "countingArray");
        timeDutty = tf.format(calendar.getTime());
        String timeEstimateDutty = AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getEnd();
        String StartDutty = AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getFrom();
        String DestinationDutty = AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getBlocks();
        String timeEnd = AllFunction.getTime(timeEstimateDutty);
        try {
            Date startTime = form.parse(form.format(calendar.getTime()));
            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            // Now formattedDate have current date/time
            String getTime = AllFunction.getTime(formattedDate);
            String getDate = AllFunction.getDate(formattedDate);
            String SaveTime = getDate + " " + getTime;
            AllFunction.storeToSharedPref(context, SaveTime, Preference.prefActualStart);
            Date endTime = form.parse(AllTaskList_adapter.getInstance().getAlltaskList().get(CountingArray).getEnd());
            diff = endTime.getTime() - startTime.getTime();
//
            if (diff < 0) {

                countingUpTimer(diff);
            } else {
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
                        countingUpTimer(finalDiff);
                    }
                }.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        tvStartDutty.setText(StartDutty);
        tvDestinationDutty.setText(DestinationDutty);
        tvTimeActualDutty.setText(timeDutty);
        tvtimeEstimateDutty.setText(timeEnd);
    }


    private void countingUpTimer(long absDif) {
        onDuty = 2;
        ivGo.setImageResource(R.drawable.circle_red);
        borderLayout1.setBackgroundResource(R.drawable.border_red);
        borderLayout2.setBackgroundResource(R.drawable.border_red);
        tvTimer.setTextColor(context.getResources().getColor(R.color.red));
        startTimes = Math.abs(absDif);
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTimes;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (startTimes / 1000);
            int minutes = seconds / 60;
            int hour = minutes / 60;
            seconds = seconds % 60;

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

    //
    @OnClick({R.id.btnDutyDone, R.id.btnDutyFails})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnDutyFails:
                ArrayList<uploadData> wrapperUpdate = getData();

                onDuty = 1;
                Date EndTime = null;
                try {
                    EndTime = form.parse(form.format(calendar.getTime()));
//                    String getTime = AllFunction.getTimes(EndTime.toString());
//                    String getDate = AllFunction.getDates(EndTime.toString());
//                    String saveEndTime = getDate + " " + getTime;
//                    AllFunction.storeToSharedPref(context, saveEndTime, Preference.prefActualEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent ReasonIntent = new Intent(Duty.this, Reason.class);
                ReasonIntent.putExtra("onDuty", onDuty);
                ReasonIntent.putExtra("onActivityJump", onActivityJump);
                startActivity(ReasonIntent);
                finish();
                break;
            case R.id.btnDutyDone:
//                Date startTime = form.parse(form.format(calendar.getTime()));
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                // Now formattedDate have current date/time
                String getTime = AllFunction.getTime(formattedDate);
                String getDate = AllFunction.getDate(formattedDate);
                String SaveTime = getDate + " " + getTime;
                AllFunction.storeToSharedPref(context, SaveTime, Preference.prefActualEnd);


                if (onDuty != 2) {

                    if (AllTaskList_adapter.getInstance().getAlltaskList().size() == countingArray + 1) {
                        ArrayList<uploadData> wrapperUpdated = getData();
                        popupMessege.ShowMessege1(context, "upload");
                        uploadSync();
                        Intent SyncIntent = new Intent(context, SyncData.class);
                        context.startActivity(SyncIntent);
                        activity.finish();
                    } else {
                        ArrayList<uploadData> wrapperUpdates = getData();
                        uploadSync();

                        AllFunction.storeToSharedPrefCount(context, "countingArray", countingArray);
                        Intent planningIntent = new Intent(Duty.this, Planning.class);
                        startActivity(planningIntent);
                        finish();
                    }

                } else {
//                    ArrayList<uploadData> wrapperUpdates =getData();
                    uploadSync();
                    AllFunction.getIntFromSharedPref(context, Preference.prefJobID);
                    Intent ReasonIntents = new Intent(Duty.this, Reason.class);
                    ReasonIntents.putExtra("onActivityJump", onActivityJump);
                    ReasonIntents.putExtra("onDuty", onDuty);
                    startActivity(ReasonIntents);
                    finish();
                }
                break;


////        calendar = Calendar.getInstance();
////        tvActTime.setText(context.getString(R.string.strStartFinish, tf.format(calendar.getTime())));
//
//        if(onDuty == 3)
//        {
//          SaveToSQLite saveToSQLite = new SaveToSQLite(context.getResources().getString(R.string.strOnSchedule), activity, context, onDuty);
//          saveToSQLite.ProcessToSQLite();
//        }else
//        {
//          Intent ReasonIntent = new Intent(Duty.this, Reason.class);
//          ReasonIntent.putExtra("onDuty", onDuty);
//          startActivity(ReasonIntent);
//          finish();
//        }
        }
    }

    public void uploadSync() {
        UploadPlanData uploadPlanData = new UploadPlanData();
        uploadPlanData.setJobID(AllFunction.getIntFromSharedPref(context, Preference.prefJobID));
        uploadPlanData.setTaskID(AllFunction.getIntFromSharedPref(context, Preference.prefTaskID));
        uploadPlanData.setActualStart(AllFunction.getStringFromSharedPref(context, Preference.prefActualStart));
        uploadPlanData.setActualEnd(AllFunction.getStringFromSharedPref(context, Preference.prefActualEnd));
        uploadPlanData.setReasonState(0);
        uploadPlanData.setReasonID(0);
        uploadPlanData.setDoneStatus(AllFunction.getStringFromSharedPref(context, Preference.prefDoneStatus));
        List<UploadPlanData> lstUpload = new ArrayList<>();
        lstUpload.add(uploadPlanData);
        AllUploadData.getInstance().setUploadData(lstUpload);
        UploadHolder uploadHolder = new UploadHolder(AllUploadData.getInstance());
        Log.d(TAG, "uploadSync: " + AllUploadData.getInstance().getUploadData().size());

//        getUpload(AllUploadData.getInstance());
    }

//    public void getUpload(AllUploadData uploadHolder) {
//        if (CheckConnection() == -1) return;
//        DataLink dataLink = AllFunction.BindingData();
////        final Call<UploadPojo> ReceiveUpload = dataLink.uploadService(uploadHolder);
////        ReceiveUpload.enqueue(new Callback<UploadPojo>() {
//            @Override
//            public void onResponse(Call<UploadPojo> call, Response<UploadPojo> response) {
//                if (response.isSuccessful()) {
//                    String msg = response.body().getCoreResponse().getMsg();
//                    Toast.makeText(context,"Alhamdullillah",Toast.LENGTH_LONG).show();
//                } else {
//                    setAllOff(context.getResources().getString(R.string.msgServerData));
//                    Toast.makeText(context,"Masih ru",Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UploadPojo> call, Throwable t) {
//
//            }
//        });
//        String s;
//    }
    private Integer CheckConnection() {
        if (AllFunction.isNetworkAvailable(context) == FixValue.TYPE_NONE) {
            setAllOff(context.getResources().getString(R.string.msgServerResponse));
            return -1;
        }
        return 0;
    }
    private void setAllOff(String strMsg) {
////            tvMsg.setText(context.getResources().getString(R.string.titleKlik));
//            ivGo.setBackgroundResource(R.drawable.buttonthick);
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
    //
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

        @Override
        public void onTick(long l) {

        }


        public void onFinish() {
            TimerToTrackLocation.cancel();

            try {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } catch (SecurityException e) {
                Log.d(TAG, "onFinish:");
            }

            if (mLastLocation != null)
                SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), null, null);

            TimerToTrackLocation.start();
        }
    }

    //
    private void SaveTrackToSQLite(String Latitude, String Longitude, String Date, String Time) {
        String strDate;
        String strTime;

        if (Date == null) {
            strDate = df.format(calendar.getTime());
            strTime = tf.format(calendar.getTime());
        } else {
            strDate = Date;
            strTime = Time;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.clear();

        hashMap.put("Latitude", Latitude);
        hashMap.put("Longitude", Longitude);
        hashMap.put("Date", strDate);
        hashMap.put("Time", strTime);

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
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),
                context.getResources().getString(R.string.strConnectionFailed), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        TimerToTrackLocation.cancel();
        tvTrackTest.setText("Lat : " + String.valueOf(mLastLocation.getLatitude()) + ", Long : " + String.valueOf(mLastLocation.getLongitude()));
        SaveTrackToSQLite(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), null, null);
//    TimerToTrackLocation.start();
    }


    //
    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(),
                    context.getResources().getString(R.string.strStartLocation), Toast.LENGTH_LONG)
                    .show();
        }
    }

    //
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

        if (!checkPlayServices()) {
            if (mGoogleApiClient.isConnected())
                mGoogleApiClient.disconnect();
        }
    }

    private ArrayList<uploadData> getData() {
        data = new ArrayList<>();
        uploadData uplData = new uploadData();
        uplData.JobID = AllFunction.getIntFromSharedPref(context, Preference.prefJobID);
        uplData.TaskID = AllFunction.getIntFromSharedPref(context, Preference.prefTaskID);
        uplData.DoneStatus = "DONE";
        uplData.ActualStart = AllFunction.getStringFromSharedPref(context, Preference.prefActualStart);
        uplData.ActualEnd = AllFunction.getStringFromSharedPref(context, Preference.prefActualEnd);
        uplData.ReasonState = 0;
        uplData.ReasonID = 0;
        data.add(uplData);
        return data;
    }

}

//class WrapperUpdate {
//    private uploadData[] data;
//
//    public uploadData[] getData() {
//        return data;
//    }
//}
//  private class CountDownClass extends CountDownTimer
//  {
//    public CountDownClass(long millisInFuture, long countDownInterval)
//    {
//      super(millisInFuture, countDownInterval);
//    }
//
//    @Override
//    public void onTick(long millisUntilFinished)
//    {
//      long millis = millisUntilFinished;
//      String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
//        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
//      tvCountTime.setText(hms);
//
//
//      hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) -
//        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
////			tvCountTimeSecond.setText(hms);
//    }
//
//    @Override
//    public void onFinish()
//    {
//      CountingTimer.cancel();
//      onDuty = 2;
//      tvCountTime.setText("00:00");
//    }
//  }
//
//  private void StartDutyCheck()
//  {
//    intDutyTask = AllFunction.getIntFromSharedPref(context, Preference.prefDutyTask);
//
//    strEstTime = AllTaskList_adapter.getInstance().getAlltaskList().get(intDutyTask - 1).getEstimateTime();
////		btnDestinyTask.setText(AllTaskList_adapter.getInstance().getAlltaskList().get(intDutyTask - 1).getDisplayName());
//    tvEstTime.setText(context.getString(R.string.strStartTask, strEstTime));
////    tvActTime.setText(context.getString(R.string.strStartFinish, ""));
//
//    calendar = Calendar.getInstance();
//    Date StartTime = null;
//    Date EndTime = null;
//    long countdown = 0;
//
//    try
//    {
//      StartTime = tf.parse(tf.format(calendar.getTime()));
//      EndTime = tf.parse(strEstTime);
//      countdown = EndTime.getTime() - StartTime.getTime();
//    }catch(ParseException e)
//    {
//      //e.printStackTrace();
//    }
//
//    if(countdown > 0)
//    {
//      onDuty = 3;
//      CountingTimer = new CountDownClass(countdown, 1000);
//      CountingTimer.start();
//    }else
//    {
//      onDuty = 2;
//    }
//  }
//