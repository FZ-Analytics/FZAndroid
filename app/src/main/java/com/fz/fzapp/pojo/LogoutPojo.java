package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.google.gson.annotations.Expose;

/**
 * Created by Heru Permana on 9/29/2017.
 */

public class LogoutPojo {
    @Expose
    private CoreResponse coreResponse;

    public CoreResponse getCoreResponse() {
        return coreResponse;
    }

    public void setCoreResponse(CoreResponse coreResponse) {
        this.coreResponse = coreResponse;
    }
}
