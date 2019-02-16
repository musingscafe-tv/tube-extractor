package com.musingscafe.tube.extractor.commands.youtube;

import com.musingscafe.tube.extractor.commands.ShellCommand;

import java.util.UUID;

public class DownloadPortion extends ShellCommand {
    private static final String TEMPLATE = "ffmpeg -i %s -ss %s -to %s -c copy %s";
    private String directLink;
    private String startTime;
    private String endTime;
    private String outputPath;

    public DownloadPortion(String directLink, String startTime, String endTime, String outputPath) {
        super(UUID.randomUUID().toString(), null);
        this.directLink = directLink;
        this.startTime = startTime;
        this.endTime = endTime;
        this.outputPath = outputPath;
    }

    @Override
    public String prepare() {
        return String.format(TEMPLATE, directLink, startTime, endTime, outputPath);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getDirectLink() {
        return directLink;
    }

    public void setDirectLink(String directLink) {
        this.directLink = directLink;
    }
}
