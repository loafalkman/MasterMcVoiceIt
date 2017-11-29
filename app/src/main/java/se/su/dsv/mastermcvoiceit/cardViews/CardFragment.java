package se.su.dsv.mastermcvoiceit.cardViews;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.mainCards.LocationCardInfo;
import se.su.dsv.mastermcvoiceit.mainCards.TempCardInfo;

/**
 * Created by annika on 2017-11-29.
 */

public class CardFragment extends Fragment {

    GPSController gpsController;

    View view;
    TempView tempView;
    LocationView locationView;

    public interface GPSController {
        void stopService();
        void startService();
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);

        try {
            gpsController = (GPSController) a;

        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " does not implement GPSController interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.card_fragment, container, false);

        tempView = new TempView(getContext());
        CardView temp = (CardView) view.findViewById(R.id.framelayout_main_tmpcommandcontainer);
        temp.addView(tempView);

        locationView = new LocationView(getContext(), gpsController);
        CardView locations = (CardView) view.findViewById(R.id.framelayout_main_locationservice);
        locations.addView(locationView);

        return view;
    }

    public void renderTemperature(TempCardInfo tempInfo) {
        tempView.tempTextView.setText(tempInfo.getTemperatureAsString());
    }

    public void renderLocation(LocationCardInfo locationInfo) {
        locationView.locationTextView.setText(locationInfo.getText());
    }
}
