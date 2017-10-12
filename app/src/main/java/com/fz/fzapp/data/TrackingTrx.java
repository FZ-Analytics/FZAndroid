package com.fz.fzapp.data;

public class TrackingTrx {

    private String Latitude;
    private String Longitude;
    private String EndDate;
    private int UserID;
    private int VehicleID;
    private int Status;
    private int LocationID;

    public TrackingTrx() {
    }

    public TrackingTrx(String latitude, String longitude, String strEndTime, int userId, int vehicleId, int statTracking) {
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.EndDate = strEndTime;
        this.VehicleID = vehicleId;
        this.UserID = userId;
        this.Status = statTracking;

    }

    public int getLocationID() {
        return LocationID;
    }

    public void setLocationID(int locationID) {
        LocationID = locationID;
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
