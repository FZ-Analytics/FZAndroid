package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Agustinus Ignat on 25-Sep-17.
 */

public class UploadPojo {
    @SerializedName("CoreResponse")
    @Expose
    private CoreResponse coreResponse;

    public CoreResponse getCoreResponse() {
        return coreResponse;
    }

    public void setCoreResponse(CoreResponse coreResponse) {
        this.coreResponse = coreResponse;
    }
}
