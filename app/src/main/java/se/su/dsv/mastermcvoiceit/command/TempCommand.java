package se.su.dsv.mastermcvoiceit.command;

import android.app.Activity;
import android.os.Bundle;

import se.su.dsv.mastermcvoiceit.sensor.TelldusSensor;

/**
 * Created by felix on 2017-11-25.
 */
public class TempCommand extends Command {
    private final String[] cmdPatterns = {"measure TEMPERATURE of sensor %d please", "TEMPERATURE sensor %d", "sensor %d"};
    private TelldusSensor sensor;

    /**
     * Automatically adds itself to the list of commands, just run "new TempCommand(--insert sensor here--);".
     */
    public TempCommand(TelldusSensor sensor) {
        super();
        this.sensor = sensor;
        makeAliases();
    }

    private void makeAliases() {
        int sensorID = sensor.getID();
        for (String pattern : cmdPatterns)
            addCommand(String.format(pattern, sensorID), this);
    }

    @Override
    public Bundle doCommand(String spokenText) {
        Bundle bundle = new Bundle();
        bundle.putInt("flag", Command.FLAG_TEMP);
        bundle.putFloat("Current TEMPERATURE", sensor.getSensorValue());

        return bundle;
    }
}
