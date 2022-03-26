package com.example.simplechatapp;

public class UserProfile {
    public String username, userUid, phonenumber;

    public UserProfile() {

    }

    public UserProfile(String username, String userUid, String phonenumber) {
        this.username = username;
        this.userUid = userUid;
        this.phonenumber = phonenumber;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


}
