package se.su.dsv.mastermcvoiceit.command;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.util.HashMap;

import se.su.dsv.mastermcvoiceit.mainCards.CardInfo;

/**
 * Created by felix on 2017-11-24.
 */

public abstract class Command {
    private static HashMap<String, Command> allCommands = new HashMap<>();

    /**
     * @param cmdAlias verbal command
     * @return command corresponding to the verbal command in cmdAlias
     */
    public static Command findCommand(String cmdAlias) {
        return allCommands.get(cmdAlias);
    }

    /**
     * This must be used if a command should be found using findCommand()
     * @param cmdAlias Spoken command
     * @param cmd Command to be connected to spoken command
     * @return Command previously mapped to the spoken command. (same as HashMaps put method)
     */
    protected static Command addCommand(String cmdAlias, Command cmd) {
        return allCommands.put(cmdAlias, cmd);
    }

    /**
     * Performs implemented command
     * @param spokenText The text that the user used to invoke this command.
     * @return Bundle with processed data
     */
    public abstract CardInfo doCommand(String spokenText, CardInfo card);

}
