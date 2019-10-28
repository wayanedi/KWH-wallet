package com.example.kwh_wallet.model;

public class User {

    private String userId;
    private String username;
    private String email;
    private String password;

    public User(){

    }

    public User(String userId, String username, String email, String password){
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
