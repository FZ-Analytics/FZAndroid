package com.fz.fzapp.sending;

import com.fz.fzapp.data.AllUploadData;
import com.fz.fzapp.data.UploadPlanData;

import java.util.List;

/**
 * Created by Agustinus Ignat on 25-Sep-17.
 */

public class UploadHolder {
    private int ActualKg;
    private List<UploadPlanData> UploadData;
    public UploadHolder(List<UploadPlanData> uploadData, int timbanganActual) {
        ActualKg = timbanganActual;
        UploadData = uploadData;
    }
}
