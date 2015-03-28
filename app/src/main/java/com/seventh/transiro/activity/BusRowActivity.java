package com.seventh.transiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.seventh.transiro.R;
import com.seventh.transiro.adapter.BusAdapter;
import com.seventh.transiro.helper.JSONExtractor;
import com.seventh.transiro.model.Bus;
import com.seventh.transiro.util.PrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BusRowActivity extends ActionBarActivity {
    private static final String TAG = "BusRowActivity";

    @InjectView(R.id.list_bus) ListView listBus;

    private List<Bus> busList = new ArrayList<Bus>();
    private JSONExtractor jsonExtractor = new JSONExtractor(this);
    private BusAdapter busAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busrow);
        ButterKnife.inject(this);

        setupActionBar();

        bindDatatoContentView();
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(PrefUtil.getLocationUSer(this));
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

    private void bindDatatoContentView() {
        try {

            JSONObject obj = new JSONObject(jsonExtractor.loadJsonFromAssets("data_bus.json"));
            JSONArray arr = obj.getJSONArray("results");
            
            for(int i=0; i< arr.length(); i++) {
                    Bus b = new Bus();
                    b.setBusCode(arr.getJSONObject(i).getString("busCode"));
                    b.setJurusan(arr.getJSONObject(i).getString("jurusan"));
                    b.setBusType(arr.getJSONObject(i).getInt("busType"));
                    b.setHalteId(arr.getJSONObject(i).getString("halteId"));
                    b.setETA(arr.getJSONObject(i).getString("eta"));
                    b.setKoridor(arr.getJSONObject(i).getInt("koridor"));
                    busList.add(b);
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

    private void detail(int position) {
        Intent detail = new Intent(this, RuteActivity.class);
        detail.putExtra("koridor", busAdapter.getItem(position).getKoridor());
        startActivity(detail);
    }
}
