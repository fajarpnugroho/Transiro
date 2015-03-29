package com.seventh.transiro.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seventh.transiro.R;

public class BusCursorAdapter extends CursorAdapter {

    private int[] imagesBusway = { R.drawable.busway_orange, R.drawable.busway_blue,
            R.drawable.busway_grey, R.drawable.busway_ijo};

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

    public BusCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.bus_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        /*

        holder.busType.setImageResource(imagesBusway[bus.getBusType()-1]);
        holder.busCode.setText(bus.getBusCode());
        holder.jurusan.setText("Jurusan : " + bus.getJurusan());
        holder.eta.setText("ETA - " + bus.getETA());*/
    }
}
