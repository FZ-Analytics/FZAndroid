package com.fz.fzapp.utils;

/**
 * Created by Agustinus Ignat on 25-Sep-17.
 */

public class uploadData {
    public int JobID;
    public int TaskID;
    public String DoneStatus;
    public String ActualStart;
    public String ActualEnd;
    public int ReasonState;
    public int ReasonID;

    @Override
    public String toString() {
        return "competitionTabData{" +
                ", JobID='" + JobID + '\'' +
                ", TaskID='" + TaskID + '\'' +
                ", DoneStatus='" + DoneStatus + '\'' +
                ", ActualStart='" + ActualStart + '\'' +
                ", avatar='" + ActualEnd + '\'' +
                ", subscriber='" + ReasonState + '\'' +
                ", date_created='" + ReasonID + '\'' +
                '}';
    }
}
