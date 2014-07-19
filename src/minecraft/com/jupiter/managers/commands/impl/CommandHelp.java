package com.jupiter.managers.commands.impl;

import com.jupiter.managers.commands.Command;

/**
 * Created by corey on 19/07/14.
 */
public class CommandHelp extends Command {

    protected CommandHelp() {
        super("Help");
    }

    @Override
    public void handleCommand(String command, String[] arguments) {
        //Handles the command
    }

    @Override
    public String getDescription() {
        return "LOL desc";
    }

    @Override
    public String getSyntax() {
        return "help";
    }
}
