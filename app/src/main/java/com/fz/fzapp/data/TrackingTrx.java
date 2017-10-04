package com.fz.fzapp.data;

public class TrackingTrx {

    private String Latitude;
    private String Longitude;
    private String EndDate;
    private int UserID;
    private int VehicleID;
    private int Status;

    public TrackingTrx( String Latitude, String Longitude, String EndDate, int UserID, int VehicleID, int Status) {

        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.EndDate = EndDate;
        this.UserID = UserID;
        this.VehicleID = VehicleID;
        this.Status = Status;

    }

    public TrackingTrx() {
    }


    public String getLatitude() {
        return Latitude;
    }


    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(int vehicleID) {
        VehicleID = vehicleID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
