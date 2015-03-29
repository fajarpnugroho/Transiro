package com.seventh.transiro.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

import com.seventh.transiro.R;
import com.seventh.transiro.helper.DistanceHelper;
import com.seventh.transiro.helper.JSONExtractor;
import com.seventh.transiro.manager.HalteManager;
import com.seventh.transiro.model.Halte;
import com.seventh.transiro.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WelcomeActivity extends ActivityWithLoadingDialog implements LocationListener {
    private static final String TAG = "WelcomeActivity";
    private static final int TIMEOUT = 2000;
    private static final int INT_SETTING = 100;

    @InjectView(R.id.greetings1)
    TextView greetings1;

    private Handler handler = new Handler();
    private HalteManager halteManager;
    private LocationManager mlocManager;
    private JSONExtractor jsonExtractor = new JSONExtractor(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        halteManager = HalteManager.getInstance(this);

        setContentView(R.layout.activity_welcome);
        ButterKnife.inject(this);
        setGreetings();
        checkLocation();
        //bypassCheckLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocManager != null) { mlocManager.removeUpdates(this); }
        halteManager.clearInstance();
    }

    private void checkLocation() {

        mlocManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
        //getting GPS status
        boolean isGPSEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //getting network status
        boolean isNetworkEnabled = mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled && isNetworkEnabled) {
            if (isNetworkEnabled) {
                mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);
                return;
            }

            if (isGPSEnabled) {
                mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
                return;
            }
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("This application need GPS enabled");
            alertDialog.setMessage("Enable GPS?");
            alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, INT_SETTING);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

    }

    private void setGreetings() {
        String sayHello = "Hello";
        greetings1.setText(sayHello);
    }

    @Override
    public void onLoadingDialogCancel() {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {

            PrefUtil.saveLocation(this, location);

            JSONObject obj = new JSONObject(jsonExtractor.loadJsonFromAssets("data_halte.json"));
            JSONArray arr = obj.getJSONArray("results");

            for (int i = 0; i < arr.length(); i++) {

                double d = DistanceHelper.distance(
                        location.getLatitude(),
                        location.getLongitude(),
                        arr.getJSONObject(i).getDouble("Lat"),
                        arr.getJSONObject(i).getDouble("Long"),
                        "K");

                //if (d <= 12) {
                    Halte h = new Halte();
                    h.setHalteName(arr.getJSONObject(i).getString("HalteName"));
                    h.setLongitude(arr.getJSONObject(i).getDouble("Long"));
                    h.setLatitude(arr.getJSONObject(i).getDouble("Lat"));
                    h.setDistanceFromUser(d);
                    halteManager.getHaltes().add(h);
                //}
            }

            mlocManager.removeUpdates(this);
            Intent lounge = new Intent(WelcomeActivity.this, LoungeActivity.class);
            startActivity(lounge);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bypassCheckLocation(){
        try {

            JSONObject obj = new JSONObject(jsonExtractor.loadJsonFromAssets("data_halte.json"));
            JSONArray arr = obj.getJSONArray("results");

            for (int i = 0; i < arr.length(); i++) {

                double d = DistanceHelper.distance(
                        -6.1863104,106.8250678,
                        arr.getJSONObject(i).getDouble("Lat"),
                        arr.getJSONObject(i).getDouble("Long"),
                        "K");

                if (d <= 12) {
                    Halte h = new Halte();
                    h.setHalteName(arr.getJSONObject(i).getString("HalteName"));
                    h.setLongitude(arr.getJSONObject(i).getDouble("Long"));
                    h.setLatitude(arr.getJSONObject(i).getDouble("Lat"));
                    h.setDistanceFromUser(d);
                    halteManager.getHaltes().add(h);
                }
            }

            Intent lounge = new Intent(WelcomeActivity.this, LoungeActivity.class);
            startActivity(lounge);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INT_SETTING) {
            checkLocation();
        }
    }
}
