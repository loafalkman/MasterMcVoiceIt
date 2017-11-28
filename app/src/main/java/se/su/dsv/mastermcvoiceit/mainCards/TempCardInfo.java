package se.su.dsv.mastermcvoiceit.mainCards;

/**
 * Created by felix on 2017-11-28.
 */

public class TempCardInfo extends CardInfo {
    float temperature;
    String answer;

    public TempCardInfo(float temperature) {
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
