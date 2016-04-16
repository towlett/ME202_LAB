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

        //OPEN DAT DB
        final DatabaseHandler db = new DatabaseHandler(this);
        rides = db.getAllRides();

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
                    long newId = db.addRide(new RideInfo(0, curLoc, curDate, rideType));
                    rides.add(new RideInfo(newId, curLoc, curDate, rideType));
                    adapter.notifyDataSetChanged();
                    Toast.makeText(RideHistoryActivity.this, R.string.ride_added_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RideHistoryActivity.this, R.string.add_ride_message, Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                deleteDialog.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteRide(rides.get(position));
                        rides.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                AlertDialog confirmDelete = deleteDialog.create();
                confirmDelete.show();
                return true;
            }
        });
    }
}
