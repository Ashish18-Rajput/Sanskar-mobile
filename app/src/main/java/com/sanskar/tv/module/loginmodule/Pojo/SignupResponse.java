package com.sanskar.tv.module.loginmodule.Pojo;

import java.io.Serializable;

/**
 * Created by appsquadz on 1/24/18.
 */

public class SignupResponse implements Serializable {
  private String fb_id;

  private String profile_picture;

  private String status;

  private String device_type;

  private String otp_verification;

  private String password;

  private String id;

  private String gmail_id;

  private String username;

  private String email;

  private String name;

  private String creation_time;

  private String gender;

  private String otp;

  private String user;

  private String device_tokken;

  private String mobile;

  private String country_code;

  private String go_live;

  public String getGo_live() {
    return go_live;
  }

  public void setGo_live(String go_live) {
    this.go_live = go_live;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  private String about;

  private String login_type;

  public String getFb_id ()
  {
    return fb_id;
  }

  public void setFb_id (String fb_id)
  {
    this.fb_id = fb_id;
  }


  public String getCountry_code() {
    return country_code;
  }

  public void setCountry_code(String country_code) {
    this.country_code = country_code;
  }

  public String getProfile_picture ()
  {
    return profile_picture;
  }

  public void setProfile_picture (String profile_picture)
  {
    this.profile_picture = profile_picture;
  }

  public String getStatus ()
  {
    return status;
  }

  public void setStatus (String status)
  {
    this.status = status;
  }

  public String getDevice_type ()
  {
    return device_type;
  }

  public void setDevice_type (String device_type)
  {
    this.device_type = device_type;
  }

  public String getOtp_verification ()
  {
    return otp_verification;
  }

  public void setOtp_verification (String otp_verification)
  {
    this.otp_verification = otp_verification;
  }

  public String getPassword ()
  {
    return password;
  }

  public void setPassword (String password)
  {
    this.password = password;
  }

  public String getId ()
  {
    return id;
  }

  public void setId (String id)
  {
    this.id = id;
  }

  public String getGmail_id ()
  {
    return gmail_id;
  }

  public void setGmail_id (String gmail_id)
  {
    this.gmail_id = gmail_id;
  }

  public String getUsername ()
  {
    return username;
  }

  public void setUsername (String username)
  {
    this.username = username;
  }

  public String getEmail ()
  {
    return email;
  }

  public void setEmail (String email)
  {
    this.email = email;
  }

  public String getName ()
  {
    return name;
  }

  public void setName (String name)
  {
    this.name = name;
  }

  public String getCreation_time ()
  {
    return creation_time;
  }

  public void setCreation_time (String creation_time)
  {
    this.creation_time = creation_time;
  }

  public String getGender ()
  {
    return gender;
  }

  public void setGender (String gender)
  {
    this.gender = gender;
  }

  public String getOtp ()
  {
    return otp;
  }

  public void setOtp (String otp)
  {
    this.otp = otp;
  }

  public String getUser ()
  {
    return user;
  }

  public void setUser (String user)
  {
    this.user = user;
  }

  public String getDevice_tokken ()
  {
    return device_tokken;
  }

  public void setDevice_tokken (String device_tokken)
  {
    this.device_tokken = device_tokken;
  }

  public String getMobile ()
  {
    return mobile;
  }

  public void setMobile (String mobile)
  {
    this.mobile = mobile;
  }

  public String getLogin_type ()
  {
    return login_type;
  }

  public void setLogin_type (String login_type)
  {
    this.login_type = login_type;
  }

  @Override
  public String toString()
  {
    return "ClassPojo [fb_id = "+fb_id+", profile_picture = "+profile_picture+", status = "+status+", device_type = "+device_type+", otp_verification = "+otp_verification+", password = "+password+", id = "+id+", gmail_id = "+gmail_id+", username = "+username+", email = "+email+", name = "+name+", creation_time = "+creation_time+", gender = "+gender+", otp = "+otp+", user = "+user+", device_tokken = "+device_tokken+", mobile = "+mobile+", login_type = "+login_type+", go_live = "+go_live+", country_code = "+country_code +", about = "+about +"]";
  }
}
