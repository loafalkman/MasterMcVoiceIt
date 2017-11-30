package se.su.dsv.mastermcvoiceit.cardModels;

import android.location.Location;

/**
 * Created by annika on 2017-11-28.
 */

public class LocationCardModel extends CardModel {
    private float distanceFromHome = -1;
    private Location location;

    public LocationCardModel() { super(CardInfoType.LOCATION); }

    public void setDistanceFromHome(float dist) {
        distanceFromHome = dist;
    }

    public String getText() {
        if (distanceFromHome >= 0)
            return "Distance from home: "+distanceFromHome+"m";
        else
            return "Waiting for GPS.";
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }
}
