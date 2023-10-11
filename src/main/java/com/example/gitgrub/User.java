package com.example.gitgrub;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private static User user = null;
    private String user_id;
    private int role_id;

    private String user_password;
    private String user_firstname;
    private String user_lastname;
    private String user_dob;
    private String user_phonenumber;
    private String user_email;
    private String user_street;
    private String user_city;
    private String state_id;
    private int user_zipcode;
    private String user_profile;
    private User(String userId,int roleID, String userPassword, String userFirstname, String userLastname, String userDob, String userPhonenumber, String userEmail, String userStreet, String userCity, String stateId, int userZipcode, String userProfilePic){

        user_id = userId;
        role_id = roleID;
        user_password = userPassword;
        user_firstname = userFirstname;
        user_lastname = userLastname;
        user_dob = userDob;
        user_phonenumber = userPhonenumber;
        user_email = userEmail;
        user_street = userStreet;
        user_city = userCity;
        state_id = stateId;
        user_zipcode = userZipcode;
        user_profile = userProfilePic;
    }

    public static synchronized void initializeUser(String userId, int roleID, String userPassword, String userFirstname, String userLastname, String userDob, String userPhonenumber, String userEmail, String userStreet, String userCity, String stateId, int userZipcode, String userProfilePic){

        if (user == null) {
            user = new User(userId,roleID,userPassword, userFirstname, userLastname, userDob, userPhonenumber, userEmail, userStreet, userCity, stateId, userZipcode, userProfilePic);
        }
    }

    public static void logOut() {
        user = null;
    }

    public static synchronized User getInstance() {
        return user;
    }

    public String getUser_profile() {   return user_profile;   }

    public void setUser_profile(String user_profile) {  this.user_profile = user_profile;}

    public int getUser_zipcode() {  return user_zipcode;}

    public void setUser_zipcode(int user_zipcode) { this.user_zipcode = user_zipcode;}

    public String getState_id() {  return state_id;}

    public void setState_id(String state_id) { this.state_id = state_id;}

    public String getUser_city() {  return user_city;}

    public void setUser_city(String user_city) {    this.user_city = user_city;}

    public String getUser_street() {    return user_street;}

    public void setUser_street(String user_street) {    this.user_street = user_street;}

    public String getUser_email() { return user_email; }

    public void setUser_email(String user_email) {  this.user_email = user_email;}

    public String getUser_phonenumber() {   return user_phonenumber;}

    public void setUser_phonenumber(String user_phonenumber) {this.user_phonenumber = user_phonenumber;}

    public String getUser_dob() {   return user_dob;}

    public void setUser_dob(String user_dob) {this.user_dob = user_dob;}

    public String getUser_lastname() {return user_lastname;}

    public void setUser_lastname(String user_lastname) {this.user_lastname = user_lastname;}

    public String getUser_firstname() {return user_firstname;}

    public void setUser_firstname(String user_firstname) {this.user_firstname = user_firstname;}

    public String getUser_password() {return user_password;}

    public void setUser_password(String user_password) {this.user_password = user_password;}

    public String getUser_id() {return user_id;}

    public void setUser_id(String user_id) {this.user_id = user_id;}
    public StringProperty adminProperty(){
        String admin;
        if(role_id == 1){
            admin = "No";
        }
        else{
            admin = "Yes";
        }
        return new SimpleStringProperty(admin);
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
