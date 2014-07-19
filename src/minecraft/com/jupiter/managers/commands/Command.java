package com.jupiter.managers.commands;

/**
 * Created by corey on 19/07/14.
 */
public abstract class Command {

    private String command;

    protected Command(String command) {
        this.command = command;
    }

    public abstract void handleCommand(String command, String[] arguments);

    public abstract String getDescription();

    public abstract String getSyntax();

    public String getCommand() {
        return command;
    }
}
