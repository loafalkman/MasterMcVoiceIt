package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.actuator.Actuator;

/**
 * Created by felix on 2017-12-08.
 */

public class ActuatorCommand extends Command {
    private final String[] cmdOnPatterns = {
            "turn on %s",
            "turn on the %s",
            "turn on %s please",
            "turn on the %s please"
    };
    private final String[] cmdOffPatterns = {
            "turn off %s",
            "turn off the %s",
            "turn off %s please",
            "turn off the %s please",
            "shut down %s",
            "shut down the %s"
    };

    private Actuator actuator;
    private boolean isTurnOnCommand;

    public ActuatorCommand(Actuator actuator, boolean turnOnCommand) {
        this.actuator = actuator;
        this.isTurnOnCommand = turnOnCommand;

        if (turnOnCommand) {
            makeAliases(cmdOnPatterns);
        } else {
            makeAliases(cmdOffPatterns);
        }
    }

    private void makeAliases(String[] cmdPatterns) {
        String actName = actuator.getName();

        for (String pattern : cmdPatterns)
            addCommand(String.format(pattern, actName), this);
    }

    @Override
    public String doCommand(String spokenText) {
        if (isTurnOnCommand) {
            actuator.setState(Integer.MAX_VALUE);
            return "turned on " + actuator.getName();
        } else {
            actuator.setState(0);
            return "turned off " + actuator.getName();
        }
    }
}
