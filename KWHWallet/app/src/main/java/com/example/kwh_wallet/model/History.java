package com.example.kwh_wallet.model;

public class History {
    private String tanggal;
    private String jumlah;

    public History(String tanggal,String jumlah){
        setTanggal(tanggal);
        setJumlah(jumlah);
    }
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
