package com.example.myapplicationfragmentslegacy_20.Model;


public class User {
    public int id;
    public String email;
    public String photoUrl;

    public User(){

    }
    public User(String email, String photoUrl) {
        this.email = email;
        this.photoUrl = photoUrl;
    }
}

