package com.example.yudikarma.aplikasidokterfkh.Model;


import android.widget.EditText;
import android.widget.RadioGroup;

public class Hewan {

    private String nama_hewan;
    private String jenis_hewan;
    private String ras;
    private String ttl;
    private String umur;
    private String alamat;
    private String jenis_lk;
    private String warnabulu;

    public String getWarnabulu() {
        return warnabulu;
    }

    public void setWarnabulu(String warnabulu) {
        this.warnabulu = warnabulu;
    }

    public Hewan(){

    }

    public String getNamaHewan() {
        return nama_hewan;
    }

    public void setNamaHewan(String namaHewan) {
        this.nama_hewan = namaHewan;
    }

    public String getJenisHewan() {
        return jenis_hewan;
    }

    public void setJenisHewan(String jenisHewan) {
        this.jenis_hewan = jenisHewan;
    }

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenis_lk() {
        return jenis_lk;
    }

    public void setJenis_lk(String jenis_lk) {
        this.jenis_lk = jenis_lk;
    }
}
