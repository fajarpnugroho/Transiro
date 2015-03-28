package com.seventh.transiro.model;

public class Bus {
    private String jurusan;
    private String busCode;
    private int busType; // 1. normal, 2. APTB 1, 3. APTB 2, 4. APTB 4
    private String halteId;
    private String ETA;
    private int koridor;

    public int getKoridor() {
        return koridor;
    }

    public void setKoridor(int koridor) {
        this.koridor = koridor;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public int getBusType() {
        return busType;
    }

    public void setBusType(int busType) {
        this.busType = busType;
    }

    public String getHalteId() {
        return halteId;
    }

    public void setHalteId(String halteId) {
        this.halteId = halteId;
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
        this.ETA = ETA;
    }
}
