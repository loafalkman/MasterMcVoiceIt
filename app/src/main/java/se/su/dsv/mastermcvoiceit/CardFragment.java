package se.su.dsv.mastermcvoiceit;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.su.dsv.mastermcvoiceit.mainCards.LocationCardInfo;
import se.su.dsv.mastermcvoiceit.mainCards.TempCardInfo;

/**
 * Created by annika on 2017-11-29.
 */

public class CardFragment extends Fragment implements MainActivity.TempController {

    View view;
    TempView tempView;
    LocationView locationView;
    TempCardInfo tempCardInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    /**
     * Inflates the fragment with it's view and calls initializeRecyclerView().
     * @param inflater used to inflate a layout object.
     * @param container the parent view.
     * @param savedInstanceState can store previous states of the fragment.
     * @return the view of the Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.card_fragment, container, false);

        initializeTempView(inflater, container);
        initializeLocationView(inflater, container);

        return view;
    }

    private void initializeTempView(LayoutInflater inflater, ViewGroup container) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_commandhistory_temp, container, false);
        tempView = new TempView(getContext(), layout);
    }

    private void initializeLocationView(LayoutInflater inflater, ViewGroup container) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.container_location_services, container, false);
        locationView = new LocationView(getContext(), layout);
    }

    public class TempView extends RelativeLayout {
        RelativeLayout layout;
        TextView tempText;

        public TempView(Context context, RelativeLayout layout) {
            super(context);

            this.layout = layout;
            this.tempText = (TextView) layout.findViewById(R.id.textview_tempitem_value);
        }
    }

    public class LocationView extends RelativeLayout {
        RelativeLayout layout;
        TextView locationText;

        public LocationView(Context context, RelativeLayout layout) {
            super(context);

            this.layout = layout;
            this.locationText = (TextView) layout.findViewById(R.id.textview_locations_description);
        }
    }

    public void renderTemperature(TempCardInfo tempInfo) {
        tempView.tempText.setText(tempInfo.getTemperatureAsString());
    }

    public void renderLocation(LocationCardInfo locationInfo) {
        locationView.locationText.setText("SOMETHING");
    }
}
