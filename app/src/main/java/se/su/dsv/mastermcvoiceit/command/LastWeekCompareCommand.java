package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHUtil;
import se.su.dsv.mastermcvoiceit.remote.sensor.Sensor;

public class LastWeekCompareCommand extends Command {
    private final String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private final String[] tempColderCmdPatterns = {"colder", "last %s"};
    private final String[] tempWarmerCmdPatterns = {"warmer", "last %s"};
    private boolean colder;

    public LastWeekCompareCommand(boolean colder) {
        super();
        this.colder = colder;

        if (colder) {
            makeAliases(tempColderCmdPatterns);
        } else {
            makeAliases(tempWarmerCmdPatterns);
        }
    }

    private void makeAliases(String[] commands) {
        for (String pattern : commands)
            for (String d : days) {
                addCommand(String.format(pattern, d), this);
            }
    }

    private String findDay(String spokenText) {

        return "";
    }

    @Override
    public String doCommand(String spokenText) {
        String day = findDay(spokenText);
        String command = "mcdiff, " + day + "135";
        String result = SSHUtil.runCommand(command, null); // OBS OBS OBS

        return null;
    }
}
