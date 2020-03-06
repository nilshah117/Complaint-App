package com.example.login_signup;

public class User_citizen {
    public String name;
    public String password;
    public String email;
    public String phoneNumber;

    public User_citizen(){

    }

    public User_citizen(String name, String password, String email, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
