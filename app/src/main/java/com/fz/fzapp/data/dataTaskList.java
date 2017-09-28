package com.fz.fzapp.data;

import java.sql.Time;
import java.util.Date;

public class dataTaskList
{

  public String id;
  public String Role_id;
  public Time time_Estimation;
  public String From;
  public String To;

  public String getId()
  {
    return id;
  }

  public String getRole_id()
  {
    return Role_id;
  }

  public Time gettime_Estimation()
  {
    return time_Estimation;
  }

  public String getFrom()
  {
    return From;
  }

  public String getTo()
  {
    return To;
  }

  @Override
  public String toString()
  {
    return "competitionLikeData{" +
      ", id='" + id + '\'' +
      ", Role_id='" + Role_id + '\'' +
      ", time_Estimation='" + time_Estimation + '\'' +
      ", From='" + From + '\'' +
      ", To='" + To + '\'' +
      '}';
  }

}
