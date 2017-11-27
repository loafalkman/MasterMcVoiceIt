package se.su.dsv.mastermcvoiceit.command;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import se.su.dsv.mastermcvoiceit.R;
import se.su.dsv.mastermcvoiceit.sensor.TelldusSensor;

/**
 * Created by felix on 2017-11-25.
 */
public class TempCommand extends Command {
    private final String[] cmdPatterns = {"measure temperature of sensor %d please", "temperature sensor %d", "sensor %d"};
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
    public Bundle doCommand(Activity parent, String spokenText) {
        Bundle bundle = new Bundle();

        bundle.putInt("flag", Command.FLAG_TEMP);
        bundle.putFloat("Current temperature", sensor.getSensorValue());

//        View skeleton = parent.getLayoutInflater().inflate(R.layout.item_commandhistory_temp, null);
//
//        // TODO: handle different values of the temperature (e.g. turn on heater when too cold, turn off when too hot)
//        // TODO: get rid of hardcoded strings (also in xml file "item_commandhistory_temp.xml")
//        float temperature = sensor.getSensorValue();
//        TextView tempDesc = skeleton.findViewById(R.id.textview_tempitem_description);
//        tempDesc.setText("Temperaturen är "+temperature+" C*");
//        skeleton.findViewById(R.id.button_tempitem_turnoff).setVisibility(View.INVISIBLE);

        return bundle;
    }
    
}
