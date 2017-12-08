package com.fz.fzapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agustinus Ignat on 25-Sep-17.
 */

public class AllUploadData {
    private static List<UploadPlanData> UploadData;
    private UploadPlanData Datanya;

    public List<UploadPlanData> getUploadData() {
        return UploadData;
    }

    public void setUploadData(List<UploadPlanData> uploadData) {
        UploadData = uploadData;
    }

    public UploadPlanData getDatanya() {
        return Datanya;
    }

    public void setDatanya(UploadPlanData datanya) {
        this.Datanya = datanya;
        this.UploadData.add(datanya);
    }

    private static AllUploadData UserInstance = new AllUploadData();

    public static AllUploadData getInstance()
    {
        return UserInstance;
    }

    private AllUploadData()
    {
    }

    public static void initUser()
    {
        UploadData = new ArrayList<>();
        UserInstance = new AllUploadData();
    }

}
