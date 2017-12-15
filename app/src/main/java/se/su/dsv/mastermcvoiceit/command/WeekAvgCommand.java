package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;

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
        return null;
    }
}
