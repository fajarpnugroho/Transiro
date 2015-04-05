package com.seventh.transiro.model;

public class Bus {
    private String jurusan;
    private String busCode;
    private int busType; // 1. normal, 2. APTB 1, 3. APTB 2, 4. APTB 4
    private String halteId;
    private String ETA;
    private String koridor;
    private int lokasi_akhir;
    private String lokasi_halte;
    private String gpsTime;
    private double longitude;
    private double latitude;
    private int speed;
    private int course;

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getLokasi_akhir() {
        return lokasi_akhir;
    }

    public void setLokasi_akhir(int lokasi_akhir) {
        this.lokasi_akhir = lokasi_akhir;
    }

    public String getLokasi_halte() {
        return lokasi_halte;
    }

    public void setLokasi_halte(String lokasi_halte) {
        this.lokasi_halte = lokasi_halte;
    }

    public String getKoridor() {
        return koridor;
    }

    public void setKoridor(String koridor) {
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
