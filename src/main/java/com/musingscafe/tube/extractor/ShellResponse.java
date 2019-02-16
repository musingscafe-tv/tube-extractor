package com.musingscafe.tube.extractor;

import java.util.UUID;

public class ShellResponse {
    private String commandName = UUID.randomUUID().toString();
    private String commandGroup = null;
    private String response = null;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandGroup() {
        return commandGroup;
    }

    public void setCommandGroup(String commandGroup) {
        this.commandGroup = commandGroup;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
