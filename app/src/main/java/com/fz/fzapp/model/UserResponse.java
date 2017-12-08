package com.fz.fzapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse
{
  @SerializedName("UserID")
  @Expose
  private Integer userID;
  @SerializedName("Name")
  @Expose
  private String name;
  @SerializedName("Phone")
  @Expose
  private String phone;
  @SerializedName("lnkRoleID")
  @Expose
  private Integer lnkRoleID;
  @SerializedName("Brand")
  @Expose
  private String brand;
  @SerializedName("Type")
  @Expose
  private String type;
  @SerializedName("VehicleID")
  @Expose
  private Integer vehicleID;
  @SerializedName("VehicleName")
  @Expose
  private String vehicleName;
  @SerializedName("TimeTrackLocation")
  @Expose
  private Integer timeTrackLocation;
  @SerializedName("MobileMenuID")
  @Expose
  private String mobileMenuID;

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getLnkRoleID() {
    return lnkRoleID;
  }

  public void setLnkRoleID(Integer lnkRoleID) {
    this.lnkRoleID = lnkRoleID;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getVehicleID() {
    return vehicleID;
  }

  public void setVehicleID(Integer vehicleID) {
    this.vehicleID = vehicleID;
  }

  public String getVehicleName() {
    return vehicleName;
  }

  public void setVehicleName(String vehicleName) {
    this.vehicleName = vehicleName;
  }

  public Integer getTimeTrackLocation() {
    return timeTrackLocation;
  }

  public void setTimeTrackLocation(Integer timeTrackLocation) {
    this.timeTrackLocation = timeTrackLocation;
  }

  public String getMobileMenuID() {
    return mobileMenuID;
  }

  public void setMobileMenuID(String mobileMenuID) {
    this.mobileMenuID = mobileMenuID;
  }

}
