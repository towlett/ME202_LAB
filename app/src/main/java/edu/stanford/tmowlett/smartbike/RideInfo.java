package edu.stanford.tmowlett.smartbike;

/**
 * Created by tomow_000 on 4/15/2016.
 */
// object to store units of ride information
public class RideInfo {

    //instance variables
    private long rideId;
    private String rideLocation;
    private String rideDate;
    private int rideIcon;

    //Constructor to set instance variables
    public RideInfo(long id_in, String location_in, String date_in, int icon_in){
        rideId = id_in;
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

    public long getRideId() {
        return rideId;
    }
}
