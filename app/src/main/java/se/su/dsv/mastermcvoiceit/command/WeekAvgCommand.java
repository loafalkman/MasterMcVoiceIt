package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by felix on 2017-12-15.
 */

public class WeekAvgCommand extends Command {
    private final String[] forCurrWeekPatterns = new String[] {"what is the average temperature this week", "average temperature this week"};
    private final String[] forLastWeekPatterns = new String[] {"what was the average temperature last week", "average temperature last week"};

    SSHConnDetails connDetails;
    boolean isForCurrWeek;

    public WeekAvgCommand(SSHConnDetails connDetails, boolean forCurrWeek) {
        this.connDetails = connDetails;
        this.isForCurrWeek = forCurrWeek;

        if (forCurrWeek) {
            makeAliases(forCurrWeekPatterns);
        } else {
            makeAliases(forLastWeekPatterns);
        }
    }

    private void makeAliases(String[] patterns) {
        for (String pattern : patterns) {
            addCommand(pattern, this);
        }
    }

    @Override
    public String doCommand(String spokenText) {
        String command = isForCurrWeek? "mcaverage CURRENT 135" : "mcaverage LAST 135";
        float result = Float.parseFloat(SSHUtil.runCommand(command, connDetails).trim());
        String sensation = result < 0 ? "cold" : "warm";

        result = Math.abs(Math.round(result * 10) / 10);

        if (isForCurrWeek)

            return "the average temperature for this week is " + result + " degrees " + sensation;
        else
            return "the average temperature for last week is " + result + " degrees " + sensation;
    }


}
