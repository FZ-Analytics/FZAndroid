package com.fz.fzapp.data;

import java.util.List;

/**
 * Created by Agustinus Ignat on 25-Sep-17.
 */

public class AllUploadData {
    private List<UploadPlanData> UploadData;

    public List<UploadPlanData> getUploadData() {
        return UploadData;
    }

    public void setUploadData(List<UploadPlanData> uploadData) {
        UploadData = uploadData;
    }

    private static AllUploadData AllUploadDataInstance = new AllUploadData();

    public static AllUploadData getInstance()
    {
        return AllUploadDataInstance;
    }

    private AllUploadData()
    {
    }

    public static void initAllUploadData()
    {
        AllUploadDataInstance = new AllUploadData();
    }
}
