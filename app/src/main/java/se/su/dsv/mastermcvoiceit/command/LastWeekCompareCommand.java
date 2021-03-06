package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

public class LastWeekCompareCommand extends Command {
    private final String[] cmdDays = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN", "LASTWEEK"};
    private final String[] verbalDays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "week"};
    private final String[] tempWarmerCmdPatterns = {
            "is it warmer than last %s",
            "is it warmer today than last %s",
            "is it warmer today compared to last %s"
    };
    private final String[] tempColderCmdPatterns = {
            "is it colder than last %s",
            "is it colder today than last %s",
            "is it colder today compared to last %S"
    };
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
            for (String d : verbalDays) {
                addCommand(String.format(pattern, d), this);
            }
    }

    private String findDay(String spokenText) {
        int dayIndex = 0;
        for (String day : verbalDays) {
            if (spokenText.toLowerCase().contains(day)) {
                return cmdDays[dayIndex];
            }
            dayIndex++;
        }

        return null;
    }

    private String generateAnswer(String result) {
        float value = Float.parseFloat(result);
        value = Math.round(value * 10) / 10;

        if (value > 0.0) {
            if (colder) {
                return "No, it is " + value + " degrees warmer";
            } else {
                return "Yes, it is " + value + " degrees warmer";
            }

        } else if (value < 0.0) {
            if (colder) {
                return "Yes, it is " + value * -1 + " degrees colder";
            } else {
                return "No, it is " + value * -1 + " degrees colder";
            }

        } else {
            return "There is no difference";
        }

    }

    @Override
    public String doCommand(String spokenText) {
        String day = findDay(spokenText);
        if (day != null) {
            String command = "mcdiff " + day + " 135";
            String result = SSHUtil.runCommand(command, connDetails);
            return generateAnswer(result);
        }

        return null;
    }
}
