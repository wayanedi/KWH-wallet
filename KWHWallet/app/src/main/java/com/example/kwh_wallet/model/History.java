package com.example.kwh_wallet.model;

public class History {
    private String tanggal;
    private String jumlah;

    private String deskripsi;

    public History(){}

    public History(String tanggal,String jumlah, String deskripsi){
        setTanggal(tanggal);
        setJumlah(jumlah);
        setDeskripsi(deskripsi);
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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
