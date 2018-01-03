package com.fz.fzapp.data;

/**
 * Created by Heru Permana on 12/20/2017.
 */

public class JobEntryData {

    private Integer userID;

    private String divID;

    private String block1;

    private String block2;

    private String readyTime;

    private Integer estmKg;

    private String dirLoc;

    private String isLastOrder;

    private String remark;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getDivID() {
        return divID;
    }

    public void setDivID(String divID) {
        this.divID = divID;
    }

    public String getBlock1() {
        return block1;
    }

    public void setBlock1(String block1) {
        this.block1 = block1;
    }

    public String getBlock2() {
        return block2;
    }

    public void setBlock2(String block2) {
        this.block2 = block2;
    }

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public Integer getEstmKg() {
        return estmKg;
    }

    public void setEstmKg(Integer estmKg) {
        this.estmKg = estmKg;
    }

    public String getDirLoc() {
        return dirLoc;
    }

    public void setDirLoc(String dirLoc) {
        this.dirLoc = dirLoc;
    }

    public String getIsLastOrder() {
        return isLastOrder;
    }

    public void setIsLastOrder(String isLastOrder) {
        this.isLastOrder = isLastOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
