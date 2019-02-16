package com.musingscafe.tube.extractor;

import com.google.common.eventbus.EventBus;
import com.musingscafe.tube.extractor.commands.youtube.DownloadPortion;
import com.musingscafe.tube.extractor.commands.youtube.GetDirectLink;

public class App {
    private boolean shouldWait = true;
    final EventBus shellEventBus = new EventBus("shellEventBus");
    final EventBus appEventBus = new EventBus("appEventBus");

    public static void main(String[] args) {
        final App app = new App();
        app.start();
    }

    public void start() {
        final ShellCommandExecutor executor = new ShellCommandExecutor(shellEventBus);
        final GetDirectLink directLink = new GetDirectLink("https://www.youtube.com/watch?v=ZevT5wvFShU");

        final DownloadPortion portion = new DownloadPortion();
        portion.setDirectLink(directLink.getUrl());
        portion.setStartTime("00:19:44");
        portion.setEndTime("00:20:21");
        portion.setOutputPath("./ZevT5wvFShU.mp4");
        executor.submit(portion, new ShellCallback() {
            @Override
            public void onComplete(ShellResponse shellResponse) {
                System.out.println(shellResponse.getCommandName());
                executor.shutDown();
                shouldWait = false;
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
                shouldWait = false;
            }
        });

        while (shouldWait) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
