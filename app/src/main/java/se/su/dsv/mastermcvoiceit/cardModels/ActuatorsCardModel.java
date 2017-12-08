package se.su.dsv.mastermcvoiceit.cardModels;


import android.util.Log;

import java.util.ArrayList;

import se.su.dsv.mastermcvoiceit.remote.actuator.Actuator;

/**
 * Created by felix on 2017-12-04.
 */

public class ActuatorsCardModel extends CardModel {

    private int[] actuatorStates;
    private String[] actuatorNames;

    private ArrayList<Actuator> actuators;

    public ActuatorsCardModel(ArrayList<Actuator> actuators) {
        super(CardModelType.ACTUATOR);
        this.actuators = actuators;
    }

    public void fetchActuatorStates() {
        actuatorStates = new int[actuators.size()];
        actuatorNames = new String[actuators.size()];

        for (int i = 0; i < actuators.size(); i++) {
            actuatorStates[i] = actuators.get(i).fetchState();
            actuatorNames[i] = actuators.get(i).getName();
        }
    }

    public String[] getActuatorNames() {
        return actuatorNames;
    }

    public int[] getActuatorStates() {
        return actuatorStates;
    }
}
