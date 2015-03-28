package com.seventh.transiro.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.seventh.transiro.R;
import com.seventh.transiro.adapter.RuteAdapter;
import com.seventh.transiro.helper.JSONExtractor;
import com.seventh.transiro.model.Rute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RuteActivity extends ActionBarActivity{
    private static final String TAG = "RuteActivity";

    @InjectView(R.id.list_bus)
    ListView listBus;
    @InjectView(R.id.ProgressBar) ProgressBar progressBar;

    private JSONExtractor jsonExtractor = new JSONExtractor(this);
    private List<Rute> rutes = new ArrayList<Rute>();
    private RuteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rute);
        ButterKnife.inject(this);

        setupActionbar();
        setupRequest();
    }

    private void setupRequest() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://itsjakarta.com/its/master_halte/"
                + getIntent().getExtras().getInt("koridor");
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
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

    private void bindDatatoContentView(String result) {
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("result");

            for (int i = 0; i < arr.length(); i++) {
                    Rute h = new Rute();
                    h.setHalteName(arr.getJSONObject(i).getString("HalteName"));
                rutes.add(h);
            }

            RuteAdapter adapter = new RuteAdapter(this, rutes);
            listBus.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setupActionbar() {
        getSupportActionBar().setTitle("Rute");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
