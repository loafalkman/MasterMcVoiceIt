package se.su.dsv.mastermcvoiceit.remote.actuator;
/**
 * Created by annika on 2017-12-01.
 */

public class TelldusActuator implements Actuator {
    private int id;
    private String name;

    public TelldusActuator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int fetchState() {
        return 0;
    }

    @Override
    public void setState(int state) {
        if (state == 1) {
            /// bablaba
        } else {
            //// blblaa
        }
    }
}
