package com.seventh.transiro.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rute implements Parcelable{
    private String halteId;
    private String halteName;

    public String getHalteId() {
        return halteId;
    }

    public void setHalteId(String halteId) {
        this.halteId = halteId;
    }

    public String getHalteName() {
        return halteName;
    }

    public void setHalteName(String halteName) {
        this.halteName = halteName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(halteId);
        parcel.writeString(halteName);
    }
}
