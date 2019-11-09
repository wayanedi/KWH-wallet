package com.example.kwh_wallet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

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


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.username);
        parcel.writeString(this.email);
        parcel.writeDouble(this.saldo);

    }

    protected User(Parcel in) {
        this.username = in.readString();
        this.email = in.readString();
        this.saldo = in.readDouble();
    }
}
