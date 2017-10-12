package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskListResponse {
    @SerializedName("divID")
    @Expose
    private String divID;
    @SerializedName("JobID")
    @Expose
    private Integer jobID;
    @SerializedName("TaskID")
    @Expose
    private Integer taskID;
    @SerializedName("From")
    @Expose
    private String from;
    @SerializedName("To")
    @Expose
    private String to;
    @SerializedName("Start")
    @Expose
    private String start;
    @SerializedName("End")
    @Expose
    private String end;
    @SerializedName("Tonnage")
    @Expose
    private Integer tonnage;
    @SerializedName("Blocks")
    @Expose
    private String blocks;
    @SerializedName("TaskSeq")
    @Expose
    private Integer taskSeq;

    @SerializedName("RequesterID")
    @Expose
    private Integer requesterID;

    @SerializedName("Name")
    @Expose
    private String name;


    public Integer getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(Integer requesterID) {
        this.requesterID = requesterID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDivID() {
        return divID;
    }

    public void setDivID(String divID) {
        this.divID = divID;
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getTonnage() {
        return tonnage;
    }

    public void setTonnage(Integer tonnage) {
        this.tonnage = tonnage;
    }

    public String getBlocks() {
        return blocks;
    }

    public void setBlocks(String blocks) {
        this.blocks = blocks;
    }

    public Integer getTaskSeq() {
        return taskSeq;
    }

    public void setTaskSeq(Integer taskSeq) {
        this.taskSeq = taskSeq;
    }
}
