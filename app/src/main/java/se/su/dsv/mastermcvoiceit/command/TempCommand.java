package se.su.dsv.mastermcvoiceit.command;

import android.app.Activity;
import android.os.Bundle;

import se.su.dsv.mastermcvoiceit.mainCards.CardInfo;
import se.su.dsv.mastermcvoiceit.mainCards.TempCardInfo;
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
    public CardInfo doCommand(String spokenText, CardInfo card) {

        float sensorValue = sensor.getSensorValue();
        TempCardInfo tempCard = (TempCardInfo) card;

        tempCard.setTemperature(sensorValue);

        return tempCard;
    }
}
