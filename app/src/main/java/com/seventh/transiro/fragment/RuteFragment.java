package com.seventh.transiro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

public class RuteFragment extends Fragment {
    private static final String TAG = "RuteFragment";
    private JSONExtractor jsonExtractor;
    private List<Rute> rutes = new ArrayList<Rute>();
    private RuteAdapter adapter;

    @InjectView(R.id.root) LinearLayout root;


    public static RuteFragment newInstance(String url){
        RuteFragment fragment = new RuteFragment();

        Bundle bundle = new Bundle();
        bundle.putString("api_url", url);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_fragment_linear, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        jsonExtractor = new JSONExtractor(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                getArguments().getString("api_url"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(TAG, response);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
