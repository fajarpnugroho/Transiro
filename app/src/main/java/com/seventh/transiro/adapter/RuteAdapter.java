package com.seventh.transiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seventh.transiro.R;
import com.seventh.transiro.model.Rute;

import java.util.List;

public class RuteAdapter extends ArrayAdapter<Rute> {
    private Context context;

    public RuteAdapter(Context context, List<Rute> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Rute halte = (Rute) getItem(position);
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.rute_item, parent, false);

            holder = new ViewHolder();
            holder.halteName = (TextView) convertView.findViewById(R.id.halte_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.halteName.setText("Halte " + halte.getHalteName());


        return convertView;
    }

    final class ViewHolder {
        public TextView halteName;
    }
}
