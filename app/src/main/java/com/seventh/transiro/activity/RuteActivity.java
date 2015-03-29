package com.seventh.transiro.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.seventh.transiro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RuteActivity extends ActionBarActivity{
    private static final String TAG = "RuteActivity";

    @InjectView(R.id.detail_bus_type) ImageView detailBusType;
    @InjectView(R.id.detail_jurusan) TextView detailJurusan;
    @InjectView(R.id.detail_eta) TextView detailEta;
    @InjectView(R.id.ProgressBar) ProgressBar progressBar;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.root) LinearLayout root;
    @InjectView(R.id.fab) ImageButton fab;

    private static final String SET_REMINDER_KEY = "set_reminder";
    private boolean mReminderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mReminderSet = savedInstanceState.getBoolean(SET_REMINDER_KEY);
        }

        setContentView(R.layout.activity_rute);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setEnterTransition(new Explode()
                    .excludeTarget(android.R.id.navigationBarBackground, true)
                    .excludeTarget(android.R.id.statusBarBackground, true));
            getWindow().setReturnTransition(new Explode()
                    .excludeTarget(android.R.id.navigationBarBackground, true));
            postponeEnterTransition();
        }

        showedHeaderView();
        setupActionbar();
        setupRequest();

        fab.setImageResource(mReminderSet ?
                R.drawable.ic_event_unset_white :
                R.drawable.ic_event_set_white );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mReminderSet) {
                    fab.setImageResource(R.drawable.ic_event_set_white);
                    animateFab(R.drawable.ic_event_unset_white,
                            R.string.toast_added_reminder);
                    mReminderSet = true;
                } else {
                    fab.setImageResource(R.drawable.ic_event_unset_white);
                    animateFab(R.drawable.ic_event_set_white,
                            R.string.toast_deleted_reminder);
                    mReminderSet = false;
                }
            }
        });
    }

    // Animates the Fab to toImg.
    // Also displays a toast at the end of the animation.
    private void animateFab(final int toImgResId, final int toastMsgResId) {
        int duration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            Animator outAnimator = ObjectAnimator.ofFloat(fab, View.ALPHA, 0f);
            outAnimator.setDuration(duration / 2);
            outAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    fab.setImageResource(toImgResId);
                }
            });
            AnimatorSet inAnimator = new AnimatorSet();
            inAnimator.playTogether(
                    ObjectAnimator.ofFloat(fab, View.ALPHA, 1f),
                    ObjectAnimator.ofFloat(fab, View.SCALE_X, 0f, 1f),
                    ObjectAnimator.ofFloat(fab, View.SCALE_Y, 0f, 1f)
            );
            inAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getText(toastMsgResId),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(outAnimator, inAnimator);
            set.start();
        } else {
            // Animation not supported in this device, so no animation
            fab.setImageResource(toImgResId);
            Toast.makeText(getApplicationContext(),
                    getResources().getText(toastMsgResId),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SET_REMINDER_KEY, mReminderSet);
        super.onSaveInstanceState(outState);
    }

    private void addListItemRute(String name) {
        TextView halteName = new TextView(this);
        halteName.setText(name);
        halteName.setTextSize(18);
        halteName.setPadding(15, 15, 15, 15);
        halteName.setBackgroundResource(R.drawable.border_textview);
        root.addView(halteName);
    }

    private void showedHeaderView() {
        switch (getIntent().getExtras().getInt("img")) {
            case 1:
                detailBusType.setImageResource(R.drawable.busway_orange);
                break;
            case 2:
                detailBusType.setImageResource(R.drawable.busway_blue);
                break;
            case 3:
                detailBusType.setImageResource(R.drawable.busway_grey);
                break;
            case 4:
                detailBusType.setImageResource(R.drawable.busway_ijo);
                break;
        }

        detailJurusan.setText(getIntent().getExtras().getString("jurusan"));
        detailEta.setText(getIntent().getExtras().getString("eta"));
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

        TextView headerRute = new TextView(this);
        headerRute.setText("Rute yang ditempuh");
        headerRute.setPadding(15, 15, 15, 15);
        headerRute.setTextSize(22);
        headerRute.setTextColor(getResources().getColor(R.color.white));
        headerRute.setBackgroundColor(getResources().getColor(R.color.dark_orange));

        root.addView(headerRute);

        try {
            JSONObject obj = new JSONObject(result);
            JSONArray arr = obj.getJSONArray("result");

            for (int i = 0; i < arr.length(); i++) {
//                Rute h = new Rute();
  //              h.setHalteName(arr.getJSONObject(i).getString("HalteName"));
    //            rutes.add(h);

                addListItemRute(arr.getJSONObject(i).getString("HalteName"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startPostponedEnterTransition();
        }

    }

    private void setupActionbar() {
        setSupportActionBar(toolbar);

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
