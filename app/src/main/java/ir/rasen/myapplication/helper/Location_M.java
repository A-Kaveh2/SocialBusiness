package ir.rasen.myapplication.helper;

import android.location.Location;

public class Location_M {
	private String latitude;
	private String longitude;

    public Location_M(){

    }
    public Location_M(Location location) {
		latitude = ""+location.getLatitude();
		longitude = ""+location.getLongitude();
	}
	public Location_M(String latitude,String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
	public String getLatitude(){
		return String.valueOf(latitude);
	}
	public String getLongitude(){
		return String.valueOf(longitude);
	}

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
