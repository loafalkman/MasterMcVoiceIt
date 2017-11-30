package se.su.dsv.mastermcvoiceit.cardModels;

/**
 * Created by felix on 2017-11-28.
 */

public class TempCardModel extends CardModel {
    float temperature;
    String answer;

    public TempCardModel(float temperature) {
        super(CardInfoType.TEMPERATURE);
        this.temperature = temperature;
        generateAnswer();
    }

    private void generateAnswer() {
        answer = "The temperature is " + temperature + "C*.";
    }

    public String getText() {
        return answer;
    }

    public String getTemperatureAsString() { return "" + temperature; }

    public void setTemperature(float value) {
        this.temperature = value;
    }
}
