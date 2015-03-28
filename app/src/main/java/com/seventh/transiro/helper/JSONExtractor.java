package com.seventh.transiro.helper;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class JSONExtractor {

    private Context context;

    public JSONExtractor(Context context) {
        this.context = context;
    }

    public String loadJsonFromAssets(String fileName) {
        String json = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
