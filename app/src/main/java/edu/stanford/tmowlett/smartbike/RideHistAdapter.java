package edu.stanford.tmowlett.smartbike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RideHistAdapter extends BaseAdapter {

    // declare instance variables
    private Context context;
    private ArrayList<RideHistoryActivity.RideInfo> rides;

    //Constructor to set instance variables
    public RideHistAdapter(Context contextIn, ArrayList<RideHistoryActivity.RideInfo> rideInfoIn){
        context = contextIn;
        rides = rideInfoIn;
    }

    @Override
    public int getCount() {
        return rides.size();
    }

    //Get current position
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
        //Get row from existing view
        View row = cV;

        //If the row currently doesn't exist then create it
        if(row == null) {
            //inflate code
            LayoutInflater in = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = in.inflate(R.layout.ride_list, par, false);

            //create new viewholder to hold ui elements
            ViewHolder holder = new ViewHolder();
            holder.rideloc = (TextView)row.findViewById(R.id.location_tv);
            holder.ridedate = (TextView)row.findViewById(R.id.date_tv);
            holder.rideicon = (ImageView)row.findViewById(R.id.bicon_iv);
            row.setTag(holder);
        }

        //Set text and picture of viewholder object
        ViewHolder h = (ViewHolder)row.getTag();
        h.rideloc.setText(rides.get(pos).getRideLocation());
        h.ridedate.setText(rides.get(pos).getRideDate());
        h.rideicon.setImageResource(rides.get(pos).getRideIcon());
        return row;
    }

    //Viewholder object to hold ui elements
    static class ViewHolder {
        public TextView rideloc;
        public TextView ridedate;
        public ImageView rideicon;
    }
}
