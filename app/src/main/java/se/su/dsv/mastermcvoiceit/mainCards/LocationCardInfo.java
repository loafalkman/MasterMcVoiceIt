package se.su.dsv.mastermcvoiceit.mainCards;

import android.location.Location;

/**
 * Created by annika on 2017-11-28.
 */

public class LocationCardInfo extends CardInfo {
    private float distanceFromHome = -1;
    private Location location;

    public LocationCardInfo() { super(CardInfoType.LOCATION); }

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
