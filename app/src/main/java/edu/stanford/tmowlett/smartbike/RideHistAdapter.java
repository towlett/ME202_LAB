package edu.stanford.tmowlett.smartbike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RideHistAdapter extends BaseAdapter {

    private Context context;
    private String[] ridelocs;
    private String[] ridedates;
    private int[] rideicons;

    public RideHistAdapter(Context contextin, String[] ridelocsin, String[] ridedatesin, int[] iconsin){
        context = contextin;
        ridelocs = ridelocsin;
        ridedates = ridedatesin;
        rideicons = iconsin;
    }

    @Override
    public int getCount() {
        return ridelocs.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View cV, ViewGroup par) {
        View row = cV;

        //If the row currently doesn't exist then create it
        if(row == null) {
            //inflate code
            LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = in.inflate(R.layout.ride_list, par, false);

            //create new viewholder
            ViewHolder holder = new ViewHolder();
            holder.rideloc = (TextView)row.findViewById(R.id.location_tv);
            holder.ridedate = (TextView)row.findViewById(R.id.date_tv);
            holder.rideicon = (ImageView)row.findViewById(R.id.bicon_iv);
            row.setTag(holder);
        }

        ViewHolder h = (ViewHolder)row.getTag();
        h.rideloc.setText(ridelocs[pos]);
        h.ridedate.setText(ridedates[pos]);
        h.rideicon.setImageResource(rideicons[pos]);
        return row;
    }

    static class ViewHolder {
        public TextView rideloc;
        public TextView ridedate;
        public ImageView rideicon;
    }
}
