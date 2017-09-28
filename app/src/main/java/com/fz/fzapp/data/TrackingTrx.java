package com.fz.fzapp.data;

/**
 * Dibuat oleh : ignat
 * Tanggal : 25-Jul-17
 * HP/WA : 0857 7070 6 777
 */
public class TrackingTrx
{
  private int LocationID;
  private String Latitude;
  private String Longitude;
  private String Date;
  private String Time;
  private int Status;

  public int getLocationID()
  {
    return LocationID;
  }

  public void setLocationID(int locationID)
  {
    LocationID = locationID;
  }

  public String getLatitude()
  {
    return Latitude;
  }

  public void setLatitude(String latitude)
  {
    Latitude = latitude;
  }

  public String getLongitude()
  {
    return Longitude;
  }

  public void setLongitude(String longitude)
  {
    Longitude = longitude;
  }

  public String getDate()
  {
    return Date;
  }

  public void setDate(String date)
  {
    Date = date;
  }

  public String getTime()
  {
    return Time;
  }

  public void setTime(String time)
  {
    Time = time;
  }

  public int getStatus()
  {
    return Status;
  }

  public void setStatus(int status)
  {
    Status = status;
  }
}
