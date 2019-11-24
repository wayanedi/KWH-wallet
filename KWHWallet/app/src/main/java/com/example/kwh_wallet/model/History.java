package com.example.kwh_wallet.model;

public class History implements Comparable<History> {
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

    @Override
    public int compareTo(History history) {
        if (history.getTanggal() == null || this.getTanggal() == null) {
            return 0;
        }
        return this.getTanggal().compareTo(history.getTanggal());
    }
}
