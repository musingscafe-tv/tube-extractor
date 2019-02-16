package com.musingscafe.tube.extractor.commands;

public abstract class ShellCommand {
    protected final String commandName;
    protected final String commandGroup;

    protected ShellCommand(String commandName, String commandGroup) {
        this.commandName = commandName;
        this.commandGroup = commandGroup;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandGroup() {
        return commandGroup;
    }

    public abstract String prepare();
}
