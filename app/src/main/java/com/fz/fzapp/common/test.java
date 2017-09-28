package com.fz.fzapp.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;
import com.fz.fzapp.service.AllFunction;
import com.fz.fzapp.utils.Preference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class test extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i=0;i<2;i++)
        {
            UploadPlanData uploadPlanData = new UploadPlanData();
            uploadPlanData.setJobID(AllFunction.getIntFromSharedPref(this, Preference.prefJobID));
            uploadPlanData.setTaskID(AllFunction.getIntFromSharedPref(this, Preference.prefTaskID));
            uploadPlanData.setActualStart(AllFunction.getStringFromSharedPref(this, Preference.prefActualStart));
            uploadPlanData.setActualEnd(AllFunction.getStringFromSharedPref(this, Preference.prefActualEnd));
            uploadPlanData.setReasonState(0);
            uploadPlanData.setReasonID(0);
            uploadPlanData.setDoneStatus(AllFunction.getStringFromSharedPref(this, Preference.prefDoneStatus));

            AllUploadData.getInstance().setDatanya(uploadPlanData);
        }

        final Gson gson = new Gson();
        Log.d("Test", "onCreate: " + gson.toJson(AllUploadData.getInstance().getUploadData()));
    }
}
