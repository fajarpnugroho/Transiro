package com.seventh.transiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.seventh.transiro.R;
import com.seventh.transiro.adapter.HalteAdapter;
import com.seventh.transiro.helper.JSONExtractor;
import com.seventh.transiro.manager.HalteManager;
import com.seventh.transiro.model.Halte;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoungeActivity extends ActivityWithLoadingDialog {
    private static final String TAG = "LoungeActivity";
    private HalteManager halteManager;

    @InjectView(R.id.list_halte) ListView listHalte;

    private JSONExtractor jsonExtractor = new JSONExtractor(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        halteManager = HalteManager.getInstance(this);
        setContentView(R.layout.activity_lounge);
        ButterKnife.inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindDatatoContent();
    }

    private void bindDatatoContent() {
        List<Halte> haltes = halteManager.getHaltes();

        Collections.sort(haltes, new Comparator<Halte>() {
            @Override
            public int compare(Halte halte, Halte halte2) {
                if (halte.getDistanceFromUser() > halte2.getDistanceFromUser()) {
                    return 1;
                } else if (halte.getDistanceFromUser() < halte2.getDistanceFromUser()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        final HalteAdapter adapter = new HalteAdapter(this, haltes);
        listHalte.setAdapter(adapter);
        listHalte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HalteManager.getInstance(getApplicationContext())
                        .setCurrentHalte(adapter.getItem(i));

                Intent chat = new Intent(getApplicationContext(), BusRowActivity.class);
                chat.putExtra("koridor", adapter.getItem(i).getKoridorNo());
                startActivity(chat);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (halteManager != null) { halteManager.clearInstance(); }
    }

    @Override
    public void onLoadingDialogCancel() {

    }
}
