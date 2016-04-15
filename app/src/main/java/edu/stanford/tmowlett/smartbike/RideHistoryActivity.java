package edu.stanford.tmowlett.smartbike;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RideHistoryActivity extends AppCompatActivity {

    //Declare instance variables
    ListView rideListView;
    ButtonRectangle addRideButton;
    EditText rideLocEditText;

    //Create Arraylist of rides
    ArrayList<RideInfo> rides = new ArrayList<RideInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        //Get UI Elements
        rideListView = (ListView)findViewById(R.id.ride_listview);
        addRideButton = (ButtonRectangle)findViewById(R.id.add_ride_button);
        rideLocEditText = (EditText)findViewById(R.id.ride_edittext);

        //Add some dummy rides
        rides.add(new RideInfo("Stanford", "04/05/2016", R.drawable.rb));
        rides.add(new RideInfo("Palo Alto", "04/06/2016", R.drawable.mb));

        //Create ride history adapter and assign to listview
        final RideHistAdapter adapter = new RideHistAdapter(this, rides);
        rideListView.setAdapter(adapter);

        //Create click listener and handle click
        addRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the location from edittext and date from the system
                String curLoc = rideLocEditText.getText().toString();
                String curDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

                if (!curLoc.isEmpty()) {
                    //Random icon assigned
                    int rideType;
                    if ((int) (Math.random() * 2) == 0) {
                        rideType = R.drawable.mb;
                    } else {
                        rideType = R.drawable.rb;
                    }

                    //Push new RideInfo object to list and notify adapter
                    rides.add(new RideInfo(curLoc, curDate, rideType));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(RideHistoryActivity.this, R.string.ride_added_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RideHistoryActivity.this, R.string.add_ride_message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // object to store units of ride information
    public class RideInfo {

        //instance variables
        private String rideLocation;
        private String rideDate;
        private int rideIcon;

        //Constructor to set instance variables
        public RideInfo(String location_in, String date_in, int icon_in){
            rideLocation = location_in;
            rideDate = date_in;
            rideIcon = icon_in;
        }

        //Getter functions for each instance variable
        public String getRideLocation(){
            return rideLocation;
        }

        public String getRideDate(){
            return rideDate;
        }

        public int getRideIcon(){
            return rideIcon;
        }
    }
}
