package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.fz.fzapp.model.DivisiRsp;
import com.fz.fzapp.model.MobileMenuRsp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Heru Permana on 12/11/2017.
 */

public class mobileMenuPojo {

    @SerializedName("CoreResponse")
    @Expose
    private CoreResponse coreResponse;
    @SerializedName("MobileMenuRsp")
    @Expose
    private List<MobileMenuRsp> mobileMenuRsp = null;
    @SerializedName("DivisiRsp")
    @Expose
    private List<DivisiRsp> divisiRsp = null;

    public CoreResponse getCoreResponse() {
        return coreResponse;
    }

    public void setCoreResponse(CoreResponse coreResponse) {
        this.coreResponse = coreResponse;
    }

    public List<MobileMenuRsp> getMobileMenuRsp() {
        return mobileMenuRsp;
    }

    public void setMobileMenuRsp(List<MobileMenuRsp> mobileMenuRsp) {
        this.mobileMenuRsp = mobileMenuRsp;
    }

    public List<DivisiRsp> getDivisiRsp() {
        return divisiRsp;
    }

    public void setDivisiRsp(List<DivisiRsp> divisiRsp) {
        this.divisiRsp = divisiRsp;
    }
}
