package com.example.kwh_wallet.model;

public class User {

    private String username;
    private String email;
    private double saldo;

    public User(){

    }


    public User(String username, String email){
        this.username = username;
        this.email = email;
        this.setSaldo(0);
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
