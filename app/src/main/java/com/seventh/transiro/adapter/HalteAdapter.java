package com.seventh.transiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seventh.transiro.R;
import com.seventh.transiro.model.Halte;

import java.text.DecimalFormat;
import java.util.List;

public class HalteAdapter extends ArrayAdapter<Halte> {
    private Context context;
    private List<Halte> haltes;

    public HalteAdapter(Context context, List<Halte> objects) {
        super(context, 0, objects);
        this.context = context;
        this.haltes = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Halte halte = (Halte) getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.halte_item, parent, false);

            holder = new ViewHolder();
            holder.halteName = (TextView) convertView.findViewById(R.id.halte_name);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.halteName.setText(halte.getHalteName() + " - Halte Transjakarta");

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        holder.distance.setText("Distance : " + df.format(halte.getDistanceFromUser()) + " KM");

        return convertView;
    }

    final class ViewHolder {
        public TextView halteName;
        public TextView distance;
    }
}
