package com.fz.fzapp.service;

//Author by Ignat.

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;
import com.fz.fzapp.pojo.UploadPojo;
import com.fz.fzapp.utils.FixValue;
import com.fz.fzapp.utils.PopupMessege;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class AllFunction extends AppCompatActivity {
    public static String DeviceInfo(Context context, int iKeperluan) {
        String tmDevice, androidId, tmSerial;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            tmDevice = "" + telephonyManager.getDeviceId();
        } catch (Exception e) {
//      tmDevice = DataAcak();
            tmDevice = "dwmG9k743Mlm23tpnj7";
        }

        try {
            androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
//      androidId = DataAcak();
            androidId = "bbe0-8fc5-5555-ww1234zyzyzy";
        }

        try {
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmDevice.hashCode());
            tmSerial = deviceUuid.toString();
        } catch (Exception e) {
//      tmSerial = DataAcak();
            tmSerial = "ffffffff-aae0-8fc5-73eb-cc8773ebcc87";
        }

        if (iKeperluan == 0)
            return tmDevice + "#" + androidId + "#" + tmSerial;
        else if (iKeperluan == 1)
            return tmDevice;
        else if (iKeperluan == 2)
            return androidId;
        else
            return tmSerial;
    }

    public static String AndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    public static String DeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
            return capitalize(model);
        else
            return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0)
            return "";

        char first = s.charAt(0);

        if (Character.isUpperCase(first))
            return s;
        else
            return Character.toUpperCase(first) + s.substring(1);
    }

//  public static void storeToSharedPref(final Context context, Object objData, String key)
//  {
//    final Gson gson = new Gson();
//    SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
//    String strObject = gson.toJson(objData);
//    editor.putString(key, strObject).commit();
//  }

    public static void storeToSharedPrefCount(final Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
        value = value + 1;
        editor.putInt(key, value).commit();

    }

//    public static void storeToSharedPrefObj(final Context context, String key, List<>) {
//        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
//        editor.putInt(key, value).commit();
//
//    }

    public static void storeToSharedPref(final Context context, String value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putString(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, long value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, boolean value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).commit();
    }

    public static void storeToSharedPref(final Context context, int value, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value).commit();
    }


    public static String getStringFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public static long getLongFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getLong(key, 99);
    }

    public static boolean getBooleanFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }

    public static int getIntFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getInt(key, 0);
    }

    public static <GenericClass> GenericClass getObjectFromSharedPref(final Context context, String key, Class<GenericClass> classType) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);

        if (prefs.contains(key)) {
            final Gson gson = new Gson();
            return gson.fromJson(prefs.getString(key, ""), classType);
        }

        return null;
    }

    public static String getDateFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(key, "1980-01-01");
    }

    public static String getTimeFromSharedPref(final Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(FixValue.strPreferenceName, Context.MODE_PRIVATE);
        return prefs.getString(key, "00:00:00");
    }

    public static void ClearPreferenceContent(final Context context) {
        context.getSharedPreferences(FixValue.strPreferenceName, 0).edit().clear().commit();
    }

    public static Bitmap ReducedImageSize(String strNamafile) {
        int intTargetWidth = 441;
        int intTargetHeight = 557;

        BitmapFactory.Options bfResult = new BitmapFactory.Options();
        bfResult.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strNamafile, bfResult);

        int intCameraWidth = bfResult.outWidth;
        int intCameraHeight = bfResult.outHeight;

        int scaleFactor = Math.min(intCameraWidth / intTargetWidth, intCameraHeight / intTargetHeight);
        bfResult.inSampleSize = scaleFactor;
        bfResult.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(strNamafile, bfResult);
    }

    public static Bitmap SetRotateImage(Bitmap bpData, String strNamafile) {
        Bitmap bpHasil;
        ExifInterface eiData = null;

        try {
            eiData = new ExifInterface(strNamafile);
        } catch (IOException e) {
            return null;
        }

        Matrix matrix = new Matrix();
        int orient = eiData.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        switch (orient) {
            case ExifInterface.ORIENTATION_NORMAL:
                matrix.setRotate(0);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
                matrix.setRotate(-90);
                break;
            default:
        }

        bpHasil = Bitmap.createBitmap(bpData, 0, 0, bpData.getWidth(), bpData.getHeight(), matrix, true);
        File file = new File(strNamafile);

        try {
            file.createNewFile();

            FileOutputStream out = new FileOutputStream(file);
            bpHasil.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        return OpenPicture(file);
    }

    public static Bitmap OpenPicture(File fopen) {
        return BitmapFactory.decodeFile(fopen.getAbsolutePath());
    }

    public static Bitmap SavePicture(Bitmap bmpGambar, String strNamafile) {
        File file = new File(strNamafile);

        try {
            Log.d("", "SimpanGambar: ");

            file.createNewFile();

            FileOutputStream out = new FileOutputStream(file);
            bmpGambar.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        return OpenPicture(file);
    }

    public static boolean InputCheck(List<EditText> lstEditText, List<String> lstMsg, Context context) {
        PopupMessege msg = new PopupMessege();

        for (int a = 0; a < lstEditText.size(); a++) {
            EditText etTemp = lstEditText.get(a);

            if (etTemp.getText().toString().matches("")) {
                msg.ShowMessege1(context, lstMsg.get(a));
                etTemp.requestFocus();
                return false;
            }
        }

        return true;
    }

    public static String RadioGroupCheck(RadioGroup rgCheck) {
        int radioButtonID;
        View radioButton;
        int idx;
        RadioButton r;
        String strTemp;

        try {
            radioButtonID = rgCheck.getCheckedRadioButtonId();
            radioButton = rgCheck.findViewById(radioButtonID);
            idx = rgCheck.indexOfChild(radioButton);
            r = (RadioButton) rgCheck.getChildAt(idx);
            strTemp = r.getText().toString();
        } catch (Exception e) {
            return "Belum Dipilih";
        }

        return strTemp;
    }

    public static int idxRadioGroup(RadioGroup rgCheck) {
        int radioButtonID;

        try {
            radioButtonID = rgCheck.getCheckedRadioButtonId();
        } catch (Exception e) {
            return 0;
        }

        return radioButtonID;
    }

    public static void RadioGroupChoose(RadioGroup rgCheck, Integer strIdx) {
        View radioButton;
        int idx;
        RadioButton r;

        try {
            radioButton = rgCheck.findViewById(strIdx);
            idx = rgCheck.indexOfChild(radioButton);
            r = (RadioButton) rgCheck.getChildAt(idx);
            r.setChecked(true);
        } catch (Exception e) {
            return;
        }
    }

    public static void setCameraParameter(Camera camera, int picwidth, int picheight, String strOrie) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("orientation", strOrie);
        parameters.set("jpeg-quality", 100);
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPictureSize(picwidth, picheight);
        camera.setParameters(parameters);
    }

    public static void setCameraDisplayOrientation(Camera camera, Context context, int FacingCamera) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(getFacingCameraId(FacingCamera), camInfo);

        if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else
            result = (camInfo.orientation - degrees + 360) % 360;

        camera.setDisplayOrientation(result);
    }

    public static int getFacingCameraId(int FacingCamera) {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == FacingCamera) {
                cameraId = i;
                break;
            }
        }

        return cameraId;
    }

    public static Point getBestPreviewSize(Camera camera) {
        int MIN_PREV_PIXELS = 480 * 320; // normal screen
        int MAX_PREV_PIXELS = 1920 * 1080; // more than large/HD screen

        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> lstSize = new ArrayList<Camera.Size>(p.getSupportedPreviewSizes());
        Collections.sort(lstSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;

                if (bPixels < aPixels)
                    return -1;

                if (bPixels > aPixels)
                    return 1;

                return 0;
            }
        });

        List<Point> lstHasil = new ArrayList<Point>();
        lstHasil.clear();

        for (Camera.Size size : lstSize) {
            int realWidth = size.width;
            int realHeight = size.height;
            int pixels = realWidth * realHeight;

            if (pixels < MIN_PREV_PIXELS || pixels > MAX_PREV_PIXELS)
                continue;

            Point pTemp = new Point(realWidth, realHeight);
            lstHasil.add(pTemp);
            Log.d("[fungsi]", "Preview Screen -> " + pTemp);
        }

        return lstHasil.get(0);
    }

    public static Point getBestPictureSize(Camera camera) {
        int MIN_PICT_PIXELS = 480 * 320; // normal screen
        int MAX_PICT_PIXELS = 1024 * 768; // more than large/HD screen

        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> lstSize = new ArrayList<Camera.Size>(p.getSupportedPictureSizes());
        Collections.sort(lstSize, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;

                if (bPixels < aPixels)
                    return -1;

                if (bPixels > aPixels)
                    return 1;

                return 0;
            }
        });

        List<Point> lstHasil = new ArrayList<Point>();
        lstHasil.clear();

        for (Camera.Size size : lstSize) {
            int realWidth = size.width;
            int realHeight = size.height;
            int pixels = realWidth * realHeight;

            if (pixels < MIN_PICT_PIXELS || pixels > MAX_PICT_PIXELS)
                continue;

            Point pTemp = new Point(realWidth, realHeight);
            lstHasil.add(pTemp);
        }

        return lstHasil.get(0);
    }

    public static int isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return FixValue.TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return FixValue.TYPE_MOBILE;
        }

        return FixValue.TYPE_NONE;
    }

    public static boolean ValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean CheckPermission(Activity activity, Context context) {
        List<String> listPermissionsNeeded = new ArrayList<>();

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.INTERNET);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.LOCATION_HARDWARE);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.LOCATION_HARDWARE);

        result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (result != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return true;
        }

        return true;
    }

    public static String getDate(String Date) {
        Date tanggal = null;
        String dateConvert = null;
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            tanggal = form.parse(Date);
            dateConvert = df.format(tanggal);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateConvert;
    }

    public static String getTime(String time) {
        Date tanggal = null;
        String dateConvert = null;
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            tanggal = form.parse(time);
            DateFormat tf = new SimpleDateFormat("hh:mm:ss");
            dateConvert = tf.format(tanggal);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateConvert;
    }


    public static int reductionDate(String start, String end) {
        Date startDate = null;
        Date endDate = null;
        int minutes = 0;
        long diff = 0;
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            startDate = form.parse(start);
            endDate = form.parse(end);
            diff = diff = endDate.getTime() - startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        minutes = (int) ((diff / 1000) / 60);
        return minutes;
    }

    public static DataLink BindingData() {
        OkHttpClient okClient = new OkHttpClient();

        okClient.newBuilder().connectTimeout(FixValue.TimeoutConnection, TimeUnit.SECONDS).
                readTimeout(FixValue.TimeoutConnection, TimeUnit.SECONDS).
                writeTimeout(FixValue.TimeoutConnection, TimeUnit.SECONDS).build();

        Retrofit retBindingData = new Retrofit.Builder().baseUrl(FixValue.Hostname).
                addConverterFactory(GsonConverterFactory.create()).
                client(okClient).build();

        return retBindingData.create(DataLink.class);
    }

    public static void uploadSync(int rsnID, Context mContext) {
        UploadPlanData uploadPlanData = new UploadPlanData();
        uploadPlanData.setJobID(AllFunction.getIntFromSharedPref(mContext, Preference.prefJobID));
        uploadPlanData.setTaskID(AllFunction.getIntFromSharedPref(mContext, Preference.prefTaskID));
        uploadPlanData.setActualStart(AllFunction.getStringFromSharedPref(mContext, Preference.prefActualStart));
        uploadPlanData.setActualEnd(AllFunction.getStringFromSharedPref(mContext, Preference.prefActualEnd));
        uploadPlanData.setReasonState(AllFunction.getIntFromSharedPref(mContext, "getReasonId"));
        uploadPlanData.setReasonID(rsnID);
        uploadPlanData.setDoneStatus(AllFunction.getStringFromSharedPref(mContext, Preference.prefDoneStatus));

        AllUploadData.getInstance().setDatanya(uploadPlanData);
        final Gson gson = new Gson();
        Log.d("Test", "getUploadData: " + gson.toJson(AllUploadData.getInstance().getUploadData()));


    }

}
