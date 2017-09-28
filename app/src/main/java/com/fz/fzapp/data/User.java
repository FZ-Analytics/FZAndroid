package com.fz.fzapp.data;

/**
 * Created by Agustinus Ignat on 29-Aug-17.
 */
public class User
{
  private int UserID;
  private String Username;
  private String Phone;
  private String Name;
  private String Password;
  private String NewPassword;

  public int getUserID()
  {
    return UserID;
  }

  public void setUserID(int userID)
  {
    UserID = userID;
  }

  public String getUsername()
  {
    return Username;
  }

  public void setUsername(String username)
  {
    Username = username;
  }

  public String getPhone()
  {
    return Phone;
  }

  public void setPhone(String phone)
  {
    Phone = phone;
  }

  public String getName()
  {
    return Name;
  }

  public void setName(String name)
  {
    Name = name;
  }

  public String getPassword()
  {
    return Password;
  }

  public void setPassword(String password)
  {
    Password = password;
  }

  public String getNewPassword()
  {
    return NewPassword;
  }

  public void setNewPassword(String newPassword)
  {
    NewPassword = newPassword;
  }

  private static User UserInstance = new User();

  public static User getInstance()
  {
    return UserInstance;
  }

  private User()
  {
  }

  public static void initUser()
  {
    UserInstance = new User();
  }
}
