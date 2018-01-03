package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Heru Permana on 12/18/2017.
 */

public class EntryResponse {

    @SerializedName("direction")
    @Expose
    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
