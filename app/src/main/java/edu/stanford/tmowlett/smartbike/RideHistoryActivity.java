package edu.stanford.tmowlett.smartbike;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RideHistoryActivity extends AppCompatActivity {

    //Declare instance variables
    ListView rideListView;
    ButtonRectangle addRideButton;
    EditText rideLocEditText;

    //Create Arraylist of rides
    ArrayList<RideInfo> rides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        //Get UI Elements
        rideListView = (ListView)findViewById(R.id.ride_listview);
        addRideButton = (ButtonRectangle)findViewById(R.id.add_ride_button);
        rideLocEditText = (EditText)findViewById(R.id.ride_edittext);

        //OPEN DAT DB and get the stored rides
        final DatabaseHandler db = new DatabaseHandler(this);
        rides = db.getAllRides();

        //Create ride history adapter and assign to listview
        final RideHistAdapter rHadapter = new RideHistAdapter(this, rides);
        rideListView.setAdapter(rHadapter);

        //Create click listener and handle click
        addRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the location from edittext and date from the system
                String curLoc = rideLocEditText.getText().toString();
                String curDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());

                if (!curLoc.isEmpty()) {
                    //Random icon assigned
                    int rideType;
                    if ((int) (Math.random() * 2) == 0) {
                        rideType = 0;
                    } else {
                        rideType = 1;
                    }

                    // Add new ride to db and get the id
                    long newId = db.addRide(new RideInfo(0, curLoc, curDate, rideType));
                    //Push new RideInfo object to list and notify adapter
                    rides.add(new RideInfo(newId, curLoc, curDate, rideType));
                    rHadapter.notifyDataSetChanged();
                    Toast.makeText(RideHistoryActivity.this, R.string.ride_added_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RideHistoryActivity.this, R.string.add_ride_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Workflow for deleting ride
        rideListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(RideHistoryActivity.this);
                deleteDialog.setTitle(R.string.confirm_delete);

                deleteDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // If okay is pressed actually delete the ride from DB and listview
                deleteDialog.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteRide(rides.get(position));
                        rides.remove(position);
                        rHadapter.notifyDataSetChanged();
                    }
                });

                AlertDialog confirmDelete = deleteDialog.create();
                confirmDelete.show();
                return true;
            }
        });
    }
}
