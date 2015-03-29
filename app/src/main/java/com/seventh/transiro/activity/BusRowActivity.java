package com.seventh.transiro.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
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
    @InjectView(R.id.toolbar) Toolbar toolbar;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowReturnTransitionOverlap(true);
            getWindow().setReenterTransition(new Fade()
                    .excludeTarget(android.R.id.navigationBarBackground, true));
        }
    }

    private void setupActionBar(){
        setSupportActionBar(toolbar);

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
        detail.putExtra("img", busAdapter.getItem(position).getBusType());
        detail.putExtra("jurusan", busAdapter.getItem(position).getJurusan());
        detail.putExtra("eta", busAdapter.getItem(position).getETA());

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
