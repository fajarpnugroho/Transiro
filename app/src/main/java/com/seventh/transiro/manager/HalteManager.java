package com.seventh.transiro.manager;

import android.content.Context;

import com.seventh.transiro.model.Halte;

import java.util.ArrayList;
import java.util.List;

public class HalteManager {
    private Context context;
    private List<Halte> haltes;

    private static HalteManager INSTANCE;

    private Halte currentHalte;

    public Halte getCurrentHalte() {
        return currentHalte;
    }

    public void setCurrentHalte(Halte currentHalte) {
        this.currentHalte = currentHalte;
    }

    public HalteManager(Context context) {
        this.context = context;
        this.haltes = new ArrayList<Halte>();
    }

    public static HalteManager getInstance(Context context){
        if (INSTANCE == null) { INSTANCE = new HalteManager(context); }
        return INSTANCE;
    }

    public List<Halte> getHaltes() { return haltes; }

    public void setHaltes(ArrayList<Halte> haltes) { this.haltes = haltes; }

    public static void clearInstance(){ if (INSTANCE != null) { INSTANCE = null; } }

}
