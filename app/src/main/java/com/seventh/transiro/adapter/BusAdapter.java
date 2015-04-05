package com.seventh.transiro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seventh.transiro.R;
import com.seventh.transiro.model.Bus;

import java.util.List;

public class BusAdapter extends ArrayAdapter<Bus> {
    private static final String TAG = "BusAdapter";
    private Context context;
    private int[] imagesBusway = { R.drawable.busway_orange, R.drawable.busway_blue,
    R.drawable.busway_grey, R.drawable.busway_ijo};

    public BusAdapter(Context context, List<Bus> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    private ViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Bus bus = (Bus) getItem(position);


        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.bus_item, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.busType.setImageResource(imagesBusway[0]);
        holder.busCode.setText(bus.getBusCode());
        holder.jurusan.setText("Jurusan : " + bus.getKoridor());
        holder.eta.setText("Estimasi waktu perjalanan : " + bus.getETA());

        return convertView;
    }



    public static class ViewHolder {
        public final ImageView busType;
        public final TextView busCode;
        public final TextView jurusan;
        public final TextView eta;

        public ViewHolder(View view) {
            busType = (ImageView) view.findViewById(R.id.bus_type);
            jurusan = (TextView) view.findViewById(R.id.jurusan);
            busCode = (TextView) view.findViewById(R.id.bus_code);
            eta = (TextView) view.findViewById(R.id.eta);
        }
    }
}
