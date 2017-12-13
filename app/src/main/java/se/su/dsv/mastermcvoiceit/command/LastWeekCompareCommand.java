package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

public class LastWeekCompareCommand extends Command {
    private final String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private final String[] tempColderCmdPatterns = {"colder", "last %s"};
    private final String[] tempWarmerCmdPatterns = {"warmer", "last %s"};
    private SSHConnDetails connDetails;
    private boolean colder;

    public LastWeekCompareCommand(SSHConnDetails sshConnDetails, boolean colder) {
        super();
        this.connDetails = sshConnDetails;
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
        for (String day : days) {
            if (spokenText.toUpperCase().contains(day)) {
                return day;
            }
        }

        return null;
    }

    private String generateAnswer(String result) {
        float value = Float.parseFloat(result);

        if (value < 0.0) {
            if (colder) {
                return "No, it was " + value + " warmer";
            } else {
                return "Yes, it was " + value + " warmer";
            }

        } else if (value > 0.0) {
            if (colder) {
                return "Yes, it was " + value + " colder";
            } else {
                return "No, it was " + value + " colder";
            }

        } else {
            return "There is no different";
        }

    }

    @Override
    public String doCommand(String spokenText) {
        String day = findDay(spokenText);
        if (day != null) {
            String command = "mcdiff, " + day + "135";
            String result = SSHUtil.runCommand(command, connDetails);
            return generateAnswer(result);
        }

        return null;
    }
}
