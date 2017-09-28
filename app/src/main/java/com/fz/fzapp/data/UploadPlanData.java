package com.fz.fzapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Agustinus Ignat on 22-Sep-17.
 */

public class UploadPlanData {

    @SerializedName("JobID")
    @Expose
    private Integer jobID;
    @SerializedName("TaskID")
    @Expose
    private Integer taskID;
    @SerializedName("DoneStatus")
    @Expose
    private String doneStatus;
    @SerializedName("ActualStart")
    @Expose
    private String actualStart;
    @SerializedName("ActualEnd")
    @Expose
    private String actualEnd;
    @SerializedName("ReasonState")
    @Expose
    private Integer reasonState;
    @SerializedName("ReasonID")
    @Expose
    private Integer reasonID;

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getDoneStatus() {
        return doneStatus;
    }

    public void setDoneStatus(String doneStatus) {
        this.doneStatus = doneStatus;
    }

    public String getActualStart() {
        return actualStart;
    }

    public void setActualStart(String actualStart) {
        this.actualStart = actualStart;
    }

    public String getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(String actualEnd) {
        this.actualEnd = actualEnd;
    }

    public Integer getReasonState() {
        return reasonState;
    }

    public void setReasonState(Integer reasonState) {
        this.reasonState = reasonState;
    }

    public Integer getReasonID() {
        return reasonID;
    }

    public void setReasonID(Integer reasonID) {
        this.reasonID = reasonID;
    }
}
