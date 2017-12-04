package se.su.dsv.mastermcvoiceit.cardViews;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.cardModels.LocationCardModel;
import se.su.dsv.mastermcvoiceit.cardModels.TempsCardModel;
import se.su.dsv.mastermcvoiceit.remote.actuator.TelldusActuator;
import se.su.dsv.mastermcvoiceit.command.TempCommand;
import se.su.dsv.mastermcvoiceit.remote.sensor.Sensor;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorList;
import se.su.dsv.mastermcvoiceit.remote.sensor.SensorType;
import se.su.dsv.mastermcvoiceit.remote.sensor.TelldusSensor;

/**
 * Created by annika on 2017-11-29.
 */

public class CardFragment extends Fragment {

    private GPSController gpsController;

    private SensorList sensorList;

    private LocationCardModel locationCardModel;
    private TempsCardModel temperaturesCardModel;


    TelldusActuator telldusActuator; // later HOME MODEL access to this

    private View fragView;
    private TempView tempView;
    private TempsView tempsView;
    private LocationView locationView;
    private ActuatorsView actuatorsView;

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
        telldusActuator = new TelldusActuator(1, "First Actuator");

        initSensors();
        initCommands();
        initCardModels();
        updateCardModels();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragView = inflater.inflate(R.layout.card_fragment, container, false);

        tempView = new TempView(getContext());
        CardView temp = (CardView) fragView.findViewById(R.id.framelayout_main_commandcontainer);
        temp.addView(tempView);

        tempsView = new TempsView(getContext());
        CardView temps = (CardView) fragView.findViewById(R.id.framelayout_main_tempscontainer);
        temps.addView(tempsView);

        locationView = new LocationView(getContext(), gpsController);
        CardView locations = (CardView) fragView.findViewById(R.id.framelayout_main_locationservice);
        locations.addView(locationView);

        actuatorsView = new ActuatorsView(getContext(), new ActuatorsView.SwitchesListener() {
            @Override
            public void onSwitchChange(int switchIndex, boolean checked) {
                Toast.makeText(getContext(), "Switch " + switchIndex + " on:" + checked, Toast.LENGTH_SHORT).show();
            }
        });
        CardView actuators = (CardView) fragView.findViewById(R.id.framelayout_main_actuatorscontainer);
        actuators.addView(actuatorsView);

        initRenderAllCards();
        return fragView;
    }

    /**
     * Should be used when sensors or actuators have been added, or when this fragment is created.
     */
    public void initRenderAllCards() {
        tempsView.setTempsList(temperaturesCardModel.getSensorNames(), temperaturesCardModel.getSensorValues());
        actuatorsView.setSwitches(new String[]{"Hello", "WOrld", "test", "Hello", "WOrld", "Hello", "WOrld"});
    }

    private void renderCard(TempsCardModel tempsModel) {
        tempsView.updateTempsList(tempsModel.getSensorValues());
    }

    private void renderCard(LocationCardModel locationModel) {
        locationView.locationTextView.setText(locationModel.getText());
    }

    private void renderCard(/*ActuatorsCardModel actuatorsModel*/) { // TODO: create actuators model
        actuatorsView.updateSwitches(new boolean[]{false, true, false, true, false, true, false});
    }

    public void renderAllCards() {
        renderCard(locationCardModel);
        renderCard(temperaturesCardModel);
        renderCard(/*actuatorsModel*/);
    }

    /**
     * should in the future ask R.pi. what sensorsById it has, maybe move this to SensorList.java to save space.
     */
    private void initSensors() {
        sensorList = new SensorList();

        sensorList.add(new TelldusSensor(2, "Living room", SensorType.TEMPERATURE));
        sensorList.add(new TelldusSensor(15, "Garage", SensorType.TEMPERATURE));
        sensorList.add(new TelldusSensor(10, "Front porch", SensorType.WIND));
    }

    private void initCardModels() {
        locationCardModel = new LocationCardModel();

        ArrayList<Sensor> tempSensors = sensorList.get(SensorType.TEMPERATURE);
        temperaturesCardModel = new TempsCardModel(tempSensors);
    }

    public void updateCardModels() {
        temperaturesCardModel.fetchSensorReadings();
    }

    // TODO: each CardsFragment should have their own list of commands?
    private void initCommands() {
        new TempCommand(sensorList.get(2));
    }
}
