package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.cardModels.CardModel;
import se.su.dsv.mastermcvoiceit.cardModels.TempCardModel;
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
    public CardModel doCommand(String spokenText, CardModel card) {

        float sensorValue = sensor.getSensorValue();
        TempCardModel tempCard = (TempCardModel) card;

        tempCard.setTemperature(sensorValue);

        return tempCard;
    }
}
