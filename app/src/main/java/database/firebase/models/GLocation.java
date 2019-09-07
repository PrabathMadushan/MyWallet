package database.firebase.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class GLocation implements Serializable {
    public enum Type implements Serializable {
        LOCATION, ROUTE
    }

    private Type type;

    private Location startLocation = null;
    private Location endLocation = null;
    private String name;


    public GLocation() {
    }

    public GLocation(Type type) {
        this.type = type;
    }

    public GLocation(String data) {
        String[] split = data.split(",");
        type = split.length == 2 ? Type.LOCATION : Type.ROUTE;
        if (type == Type.LOCATION) {
            setLocation(data);
        } else {
            setRoute(data);
        }
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    private void setLocation(String data) {
        String[] split = data.split(",");
        double lat = Double.parseDouble(split[0]);
        double lng = Double.parseDouble(split[1]);
        startLocation = new Location(lat, lng);
    }

    private void setRoute(String data) {
        String[] split = data.split(",");
        double lat1 = Double.parseDouble(split[0]);
        double lng1 = Double.parseDouble(split[1]);
        double lat2 = Double.parseDouble(split[2]);
        double lng2 = Double.parseDouble(split[3]);
        startLocation = new Location(lat1, lng1);
        startLocation = new Location(lat2, lng2);
    }

    public void setLocation(LatLng location) {
        double lat = location.latitude;
        double lng = location.longitude;
        startLocation = new Location(lat, lng);
    }

    public void setStartLocationX(LatLng location) {
        double lat = location.latitude;
        double lng = location.longitude;
        startLocation = new Location(lat, lng);
    }

    public void setEndLocationX(LatLng location) {
        double lat = location.latitude;
        double lng = location.longitude;
        endLocation = new Location(lat, lng);
    }

    public void setRoute(LatLng start, LatLng end) {

        double lat1 = start.latitude;
        double lng1 = start.longitude;
        double lat2 = end.latitude;
        double lng2 = end.longitude;
        startLocation = new Location(lat1, lng1);
        startLocation = new Location(lat2, lng2);
    }


    public LatLng getStartLocation() {
        if (startLocation == null) {
            return null;
        } else {
            return new LatLng(startLocation.getLatitude(), startLocation.getLongitude());
        }
    }

    public LatLng getEndLocation() {
        if (endLocation == null) {
            return null;
        } else {
            return new LatLng(endLocation.getLatitude(), endLocation.getLongitude());
        }

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        if (type == Type.LOCATION) {
            return startLocation.toString();
        } else {
            return startLocation.toString() + "," + endLocation.toString();
        }
    }

}
