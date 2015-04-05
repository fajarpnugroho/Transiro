package com.seventh.transiro.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nispok.snackbar.Snackbar;
import com.seventh.transiro.R;
import com.seventh.transiro.adapter.BusAdapter;
import com.seventh.transiro.helper.DistanceHelper;
import com.seventh.transiro.helper.JSONExtractor;
import com.seventh.transiro.manager.HalteManager;
import com.seventh.transiro.model.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BusRowActivity extends ActionBarActivity {
    private static final String TAG = "BusRowActivity";
    private static final int SCHEDULE = 60000;

    @InjectView(R.id.list_bus) ListView listBus;
    @InjectView(R.id.toolbar) Toolbar toolbar;

    private List<Bus> busList = new ArrayList<Bus>();
    private JSONExtractor jsonExtractor = new JSONExtractor(this);
    private BusAdapter busAdapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busrow);
        ButterKnife.inject(this);

        setupActionBar();

        trackBusLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowReturnTransitionOverlap(true);
            getWindow().setReenterTransition(new Fade()
                    .excludeTarget(android.R.id.navigationBarBackground, true));
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                busAdapter.clear();
                listBus.setAdapter(null);
                busAdapter.notifyDataSetChanged();
                trackBusLocation();
            }
        }, SCHEDULE);
    }

    private void trackBusLocation() {
        Snackbar.with(getApplicationContext())
                .text("Scheduling time arrival...")
                .show(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://202.51.116.138:8088/jsc.asp?rq=jakartasmartcity&id=476A837BE937ED73";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(TAG, response);
                        bindDatatoContentView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void setupActionBar(){
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(HalteManager.getInstance(getApplicationContext())
                .getCurrentHalte().getHalteName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindDatatoContentView(String response) {
        try {

            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("buswaytracking");

            Log.v("KORIDOR", HalteManager.getInstance(getApplicationContext()).getCurrentHalte()
                    .getKoridorNo());
            
            for(int i=0; i< arr.length(); i++) {
                    if (arr.getJSONObject(i).getString("buscode").equals("")) continue;

                    if (arr.getJSONObject(i).getString("koridor") == null) continue;

                    if (arr.getJSONObject(i).getString("koridor").equalsIgnoreCase("XX")) continue;

                    if (arr.getJSONObject(i).getInt("speed") == 0) continue;

                    if (HalteManager.getInstance(getApplicationContext()).getCurrentHalte().getKoridorNo()
                            .equals(arr.getJSONObject(i).getString("koridor"))) {
                        Bus b = new Bus();
                        b.setBusCode(arr.getJSONObject(i).getString("buscode"));
                        b.setKoridor(arr.getJSONObject(i).getString("koridor"));
                        b.setGpsTime(arr.getJSONObject(i).getString("gpsdatetime"));
                        b.setLongitude(arr.getJSONObject(i).getDouble("longitude"));
                        b.setLatitude(arr.getJSONObject(i).getDouble("latitude"));
                        b.setSpeed(arr.getJSONObject(i).getInt("speed"));
                        b.setCourse(arr.getJSONObject(i).getInt("course"));
                        b.setETA(calculatedTimeArrival(getDistanceBusToHalte(
                                        b.getLongitude(), b.getLatitude()),
                                        b.getSpeed()));
                        busList.add(b);
                    }
            }

            if (busList.size() == 0) {
                Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
            }

            busAdapter = new BusAdapter(this, busList);
            listBus.setAdapter(busAdapter);
            listBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    detail(i);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double getDistanceBusToHalte(double longitude, double latitude){
        return DistanceHelper.distance(
                latitude,
                longitude,
                HalteManager.getInstance(this).getCurrentHalte().getLatitude(),
                HalteManager.getInstance(this).getCurrentHalte().getLongitude(),
                "K"
        );
    }

    private String calculatedTimeArrival(double distance, double speed) {
        double time = (distance / speed) * 60;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, (int) (time/60));
        cal.set(Calendar.MINUTE, (int) (time % 60));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String hhmm = sdf.format(cal.getTime());

        return hhmm;
    }

    private void detail(int position) {
        Intent detail = new Intent(this, RuteActivity.class);
        detail.putExtra("koridor", busAdapter.getItem(position).getKoridor());
        detail.putExtra("img", busAdapter.getItem(position).getBusType());
        detail.putExtra("jurusan", busAdapter.getItem(position).getJurusan());
        detail.putExtra("eta", busAdapter.getItem(position).getETA());
        detail.putExtra("lokasi_akhir", busAdapter.getItem(position).getLokasi_akhir());

        //startActivity(detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(defineExitTransition(position));
        }

        View listItemView = ((ListView) findViewById(R.id.list_bus))
                .getChildAt(position);

        BusAdapter.ViewHolder viewHolder = (BusAdapter.ViewHolder) listItemView.getTag();
        View iconView = viewHolder.busType;
        View textView = viewHolder.jurusan;
        View etaView = viewHolder.eta;

        Resources res = getResources();
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair.create(iconView, res.getString(R.string.transition_image)),
                Pair.create(textView, res.getString(R.string.transition_jurusan)),
                Pair.create(etaView, res.getString(R.string.transition_eta))
        );
        ActivityCompat.startActivity(this, detail, activityOptions.toBundle());
    }

    // Returns the Transition that descibes how MainActivity acts when it goes to DetailActivity
    // Takes in the position in the list that was clicked (counted from the visible portion of the list)
    // The animation is a splitting apart of the List View
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    Transition defineExitTransition(int visiblePosition) {
        ListView lv = (ListView) findViewById(R.id.list_bus);
        Transition upperTrans = new Slide(Gravity.TOP)
                .addTarget(findViewById(R.id.toolbar))
                .setDuration(800);
        for (int i=0; i<visiblePosition; i++) {
            upperTrans.addTarget(lv.getChildAt(i));
        }
        Transition lowerTrans = new Slide(Gravity.BOTTOM)
                .setDuration(800);
        for (int i=visiblePosition+1; i<lv.getChildCount(); i++) {
            lowerTrans.addTarget(lv.getChildAt(i));
        }
        Transition middleTrans = new Fade().setDuration(100)
                .addTarget(lv.getChildAt(visiblePosition));
        TransitionSet ts = new TransitionSet();
        ts.addTransition(upperTrans)
                .addTransition(lowerTrans)
                .addTransition(middleTrans)
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .excludeTarget(android.R.id.navigationBarBackground, true)
                .excludeTarget(android.R.id.statusBarBackground, true);
        return ts;
    }
}
