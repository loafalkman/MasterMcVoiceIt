package se.su.dsv.mastermcvoiceit.cardViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import se.su.dsv.mastermcvoiceit.R;

/**
 * Created by annika on 2017-11-29.
 */

public class LocationView extends RelativeLayout {
    CardFragment.GPSController gpsController;
    LayoutInflater mInflater;
    TextView locationTextView;
    Switch GPSSwitch;


    public LocationView(Context context, CardFragment.GPSController controller) {
        super(context);
        gpsController = controller;
        mInflater = LayoutInflater.from(context);
        init();
    }

    public LocationView(Context context, AttributeSet attrs, int defStyle)  {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public LocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View v = mInflater.inflate(R.layout.container_location_services, this, true);
        locationTextView = (TextView) v.findViewById(R.id.textview_locations_description);

        GPSSwitch = (Switch) v.findViewById(R.id.location_switch);
        GPSSwitch.setChecked(true);
        GPSSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    gpsController.startGPS();
                } else {
                    gpsController.stopGPS();
                }
            }
        });
    }
}