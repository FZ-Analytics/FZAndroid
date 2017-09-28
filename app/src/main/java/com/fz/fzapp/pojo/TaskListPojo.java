package com.fz.fzapp.pojo;

import com.fz.fzapp.model.CoreResponse;
import com.fz.fzapp.model.TaskListResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TaskListPojo {
    @SerializedName("CoreResponse")
    @Expose
    private CoreResponse coreResponse;
    @SerializedName("TaskListResponse")
    @Expose
    private List<TaskListResponse> taskListResponse = null;

    public CoreResponse getCoreResponse() {
        return coreResponse;
    }

    public void setCoreResponse(CoreResponse coreResponse) {
        this.coreResponse = coreResponse;
    }

    public List<TaskListResponse> getTaskListResponse() {
        return taskListResponse;
    }

    public void setTaskListResponse(List<TaskListResponse> taskListResponse) {
        this.taskListResponse = taskListResponse;
    }
}
