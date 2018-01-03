package com.fz.fzapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.fz.fzapp.Smart_Fit.Estimation_Entry;
import com.fz.fzapp.Smart_Fit.Job_Entry;
import com.fz.fzapp.Smart_Fit.webView_Smart_Fit;
import com.fz.fzapp.model.DivisiRsp;
import com.fz.fzapp.model.MobileMenuRsp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heru Permana on 12/14/2017.
 */

public class Smart_fit_Conditional {
    private String webUrl;
    private String id;
    Activity activity;
    Context context;
    MobileMenuRsp mobileMenuRsp;
    String gsonToString;

    public Smart_fit_Conditional(String tittle, String id, Activity activity, Context context, MobileMenuRsp mobileMenuRsp, String gsonToString) {
        this.webUrl = tittle;
        this.id = id;
        this.activity = activity;
        this.context = context;
        this.mobileMenuRsp = mobileMenuRsp;
        this.gsonToString = gsonToString;
    }

    public void smatfitMenu() {
        String id = String.valueOf(mobileMenuRsp.getId());
        if (id == "5") {
            Intent IntentJobEntry = new Intent(context, Job_Entry.class);
            IntentJobEntry.putExtra("gsonToString",gsonToString);
//            IntentJobEntry.putExtra("webUrl", webUrl);
            context.startActivity(IntentJobEntry);
        } else if (id == "2") {
            Intent IntentExtimationEntry = new Intent(context, Estimation_Entry.class);
            context.startActivity(IntentExtimationEntry);
        } else {
            Intent IntentToWebview = new Intent(context, webView_Smart_Fit.class);
            IntentToWebview.putExtra("webUrl", webUrl);
            context.startActivity(IntentToWebview);
        }
    }


}
