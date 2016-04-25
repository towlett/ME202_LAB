package edu.stanford.tmowlett.smartbike;

// object to store units of ride information
public class RideInfo {

    //instance variables
    private String rideId;
    private String rideLocation;
    private String rideDate;
    private long rideIcon;

    public RideInfo() {}

    //Constructor to set instance variables
    public RideInfo(String id_in, String location_in, String date_in, long icon_in){
        rideId = id_in;
        rideLocation = location_in;
        rideDate = date_in;
        rideIcon = icon_in;
    }

    //Constructor without id
    public RideInfo(String location_in, String date_in, long icon_in) {
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

    // Function to take ride icon int from DB and return the appropriate drawable
    public long getRideIcon(){
        return rideIcon;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideIdin) {
        rideId = rideIdin;
    }

    public int obtainRideDrawable(){
        if (rideIcon == 0) {
            return R.drawable.mb;
        } else {
            return R.drawable.rb;
        }
    }
}
