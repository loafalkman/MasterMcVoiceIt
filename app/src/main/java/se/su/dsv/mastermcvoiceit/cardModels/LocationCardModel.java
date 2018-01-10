package se.su.dsv.mastermcvoiceit.cardModels;

/**
 * Created by annika on 2017-11-28.
 */

public class LocationCardModel extends CardModel {
    private float distanceFromHome = -1;

    public LocationCardModel() { super(CardModelType.LOCATION); }

    public void setDistanceFromHome(float dist) {
        distanceFromHome = dist;
    }

    public String getText() {
        if (distanceFromHome >= 0)
            return "Distance from home: " + Math.floor(distanceFromHome/10) / 100 +" km";
        else
            return "Waiting for GPS.";
    }
}
