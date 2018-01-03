package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Heru Permana on 12/11/2017.
 */

public class MobileMenuRsp {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("WebURL")
    @Expose
    private String webURL;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }
}
