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
import se.su.dsv.mastermcvoiceit.cardModels.ActuatorsCardModel;
import se.su.dsv.mastermcvoiceit.cardModels.LocationCardModel;
import se.su.dsv.mastermcvoiceit.cardModels.TempsCardModel;
import se.su.dsv.mastermcvoiceit.remote.actuator.Actuator;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorList;
import se.su.dsv.mastermcvoiceit.remote.actuator.ActuatorType;
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
    private ActuatorList actuatorList;

    private LocationCardModel locationCardModel;
    private TempsCardModel temperaturesCardModel;
    private ActuatorsCardModel actuatorsModel;

    private View fragView;
    private TempView tempView;
    private TempsView tempsView;
    private LocationView locationView;
    private ActuatorsView actuatorsView;

    public interface GPSController {
        void stopGPS();

        void startGPS();
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

        initSensors();
        initActuators();
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
                actuatorList.get(ActuatorType.POWER_SWITCH).get(switchIndex).setState(checked ? 1 : 0);
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
        actuatorsView.setSwitches(actuatorsModel.getActuatorNames());
    }

    private void renderCard(TempsCardModel tempsModel) {
        tempsView.updateTempsList(tempsModel.getSensorValues());
    }

    private void renderCard(LocationCardModel locationModel) {
        locationView.locationTextView.setText(locationModel.getText());
    }

    private void renderCard(ActuatorsCardModel actuatorsModel) {
        actuatorsView.updateSwitches(actuatorsModel.getActuatorStates());
    }

    public void renderAllCards() {
        renderCard(locationCardModel);
        renderCard(temperaturesCardModel);
        renderCard(actuatorsModel);
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

    private void initActuators() {
        actuatorList = new ActuatorList();

        actuatorList.add(new TelldusActuator(1, "bedroom light", ActuatorType.POWER_SWITCH));
        actuatorList.add(new TelldusActuator(42, "coffeemaker", ActuatorType.POWER_SWITCH));
        actuatorList.add(new TelldusActuator(5, "central heating", ActuatorType.HEATER));
        actuatorList.add(new TelldusActuator(7, "element", ActuatorType.POWER_SWITCH));
    }

    private void initCardModels() {
        locationCardModel = new LocationCardModel();

        ArrayList<Sensor> tempSensors = sensorList.get(SensorType.TEMPERATURE);
        temperaturesCardModel = new TempsCardModel(tempSensors);

        ArrayList<Actuator> actuators = actuatorList.get(ActuatorType.POWER_SWITCH);
        actuatorsModel = new ActuatorsCardModel(actuators);
    }

    public void updateCardModels() {
        temperaturesCardModel.fetchSensorReadings();
        actuatorsModel.fetchActuatorStates();
    }

    // TODO: each CardsFragment should have their own list of commands?
    private void initCommands() {
        new TempCommand(sensorList.get(2));
    }
}
