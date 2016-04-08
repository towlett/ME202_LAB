package edu.stanford.tmowlett.smartbike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RideHistoryActivity extends AppCompatActivity {

    ListView rideListView;
    Button addRideButton;
    EditText rideLocEditText;

    String[] ride_locs = {"Stanford", "PA", "Oakland", "Thadland", "Candyland"};
    String[] ride_dates = {"Today", "Tomorrow", "Yesterday", "Today", "Yesterday"};
    int[] ride_icons = {R.drawable.rb, R.drawable.mb, R.drawable.rb, R.drawable.rb, R.drawable.mb};
    //ArrayList<String> ride_locs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        rideListView = (ListView)findViewById(R.id.ride_listview);
        addRideButton = (Button)findViewById(R.id.add_ride_button);
        rideLocEditText = (EditText)findViewById(R.id.ride_edittext);

        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, ride_locs);
        //rideListView.setAdapter(adapter);

        final RideHistAdapter adapter = new RideHistAdapter(this, ride_locs, ride_dates, ride_icons);
        rideListView.setAdapter(adapter);

        addRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
