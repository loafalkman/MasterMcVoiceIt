package se.su.dsv.mastermcvoiceit.cardViews;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.cardModels.LocationCardModel;
import se.su.dsv.mastermcvoiceit.cardModels.TempsCardModel;

/**
 * Created by annika on 2017-11-29.
 */

public class CardFragment extends Fragment {

    GPSController gpsController;

    View view;
    TempView tempView;
    TempsView tempsView;
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
        CardView temp = (CardView) view.findViewById(R.id.framelayout_main_commandcontainer);
        temp.addView(tempView);

        tempsView = new TempsView(getContext());
        CardView temps = (CardView) view.findViewById(R.id.framelayout_main_tempscontainer);
        temps.addView(tempsView);

        locationView = new LocationView(getContext(), gpsController);
        CardView locations = (CardView) view.findViewById(R.id.framelayout_main_locationservice);
        locations.addView(locationView);

        return view;
    }

    public void renderTemperatures(TempsCardModel tempsModel) {
        tempsView.setTempsList(tempsModel.getSensorNames(), tempsModel.getSensorValues());
    }

    public void renderLocation(LocationCardModel locationModel) {
        locationView.locationTextView.setText(locationModel.getText());
    }
}
