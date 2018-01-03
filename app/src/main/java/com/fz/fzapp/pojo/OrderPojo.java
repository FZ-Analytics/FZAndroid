package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.fz.fzapp.model.EntryResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Heru Permana on 12/18/2017.
 */

public class OrderPojo {


    @SerializedName("CoreResponse")
    @Expose
    private CoreResponse coreResponse;
    @SerializedName("EntryResponse")
    @Expose
    private List<EntryResponse> entryResponse = null;

    public CoreResponse getCoreResponse() {
        return coreResponse;
    }

    public void setCoreResponse(CoreResponse coreResponse) {
        this.coreResponse = coreResponse;
    }

    public List<EntryResponse> getEntryResponse() {
        return entryResponse;
    }

    public void setEntryResponse(List<EntryResponse> entryResponse) {
        this.entryResponse = entryResponse;
    }


}
