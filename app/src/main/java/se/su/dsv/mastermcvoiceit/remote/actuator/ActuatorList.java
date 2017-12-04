package se.su.dsv.mastermcvoiceit.remote.actuator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by felix on 2017-12-04.
 */

public class ActuatorList {
    private HashMap<Integer, Actuator> actuatorById = new HashMap<>();
    private HashMap<ActuatorType, ArrayList<Actuator>> actuatorByType = new HashMap<>();

    public void add(Actuator actuator) {
        if (actuatorById.put(actuator.getID(), actuator) != null)
            throw new RuntimeException("there was at least 2 actuators with same ID, not allowed");

        ActuatorType actuatorType = actuator.getType();
        ArrayList<Actuator> sameTypeList = actuatorByType.get(actuatorType);
        if (sameTypeList == null) {
            sameTypeList = new ArrayList<>();
            actuatorByType.put(actuatorType, sameTypeList);
        }
        sameTypeList.add(actuator);
    }

    public void remove(Actuator actuator) {
        actuatorById.remove(actuator.getID());

        ArrayList<Actuator> sameTypeList = actuatorByType.get(actuator.getType());
        sameTypeList.remove(actuator);
    }

    public Actuator get(int id) {
        return actuatorById.get(id);
    }

    public ArrayList<Actuator> get(ActuatorType type) {
        return actuatorByType.get(type);
    }

}
