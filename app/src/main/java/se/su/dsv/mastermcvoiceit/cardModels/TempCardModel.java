package se.su.dsv.mastermcvoiceit.cardModels;

import se.su.dsv.mastermcvoiceit.sensor.Sensor;

/**
 * Created by felix on 2017-11-28.
 */

public class TempCardModel extends CardModel {
    Sensor sensor;
    float temperature;

    public TempCardModel(Sensor tempSensr) {
        super(CardModelType.TEMPERATURE);
        sensor = tempSensr;
        updateSensorReadings();
    }

    public void updateSensorReadings() {
        temperature = sensor.fetchSensorValue();
    }

    public String getTemperatureAsString() {
        return "" + temperature;
    }

    public String getSensorName() {
        return sensor.getName();
    }
}
