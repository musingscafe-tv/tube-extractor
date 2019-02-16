package com.musingscafe.tube.extractor.commands.youtube;

import com.musingscafe.tube.extractor.commands.ShellCommand;

import java.util.UUID;

public class GetDirectLink extends ShellCommand {
    private static final String TEMPLATE = "youtube-dl -g '%s'";
    private String url;

    public GetDirectLink(String url) {
        super(UUID.randomUUID().toString(), null);
        this.url = url;
    }

    @Override
    public String prepare() {
        return String.format(TEMPLATE, url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
