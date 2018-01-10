package se.su.dsv.mastermcvoiceit.command;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by felix on 2017-12-15.
 */

public class CompareWeekTempCommand extends Command {
    private final String[] tempWarmerCmdPatterns = {
            "is this week warmer than last week",
            "is this week warmer compared to last week"
    };
    private final String[] tempColderCmdPatterns = {
            "is this week colder than last week",
            "is this week warmer compared to last week"
    };
    private SSHConnDetails connDetails;
    private boolean colder;

    public CompareWeekTempCommand(SSHConnDetails sshConnDetails, boolean colder) {
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
            addCommand(pattern, this);
    }

    private String generateAnswer(String result) {
        float value = Float.parseFloat(result);

        if (value > 0.0) {
            if (colder) {
                return "No, this week is on average " + value + " degrees warmer";
            } else {
                return "Yes, this week is on average " + value + " degrees warmer";
            }

        } else if (value < 0.0) {
            if (colder) {
                return "Yes, this week is on average " + value * -1 + " degrees colder";
            } else {
                return "No, this week is on average " + value * -1 + " degrees colder";
            }

        } else {
            return "There is no difference between the averages";
        }

    }

    @Override
    public String doCommand(String spokenText) {
        String command = "mcdiff WEEKS 135";
        String result = SSHUtil.runCommand(command, connDetails);
        return generateAnswer(result);
    }
}
