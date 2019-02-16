package com.musingscafe.tube.extractor;

import com.google.common.eventbus.EventBus;
import com.musingscafe.tube.extractor.commands.youtube.DownloadPortion;
import com.musingscafe.tube.extractor.commands.youtube.GetDirectLink;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.get;

public class App {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(App.class);
    private boolean shouldWait = true;
    final EventBus shellEventBus = new EventBus("shellEventBus");
    final EventBus appEventBus = new EventBus("appEventBus");

    public static void main(String[] args) {
        Spark.staticFiles.location("/public");
        get("/hello", (req, res) -> {
            App app = new App();
            app.start();
            return "Hello";
        });
        get("/helloworld", (req, res) -> "Hello World");

    }

    public void start() {
        final ShellCommandExecutor executor = new ShellCommandExecutor(shellEventBus);
        final GetDirectLink directLink = new GetDirectLink("https://www.youtube.com/watch?v=MsnpHI8E7ec");

        final DownloadPortion portion = new DownloadPortion();
        portion.setDirectLink(directLink.getUrl());
        portion.setStartTime("00:01:44");
        portion.setEndTime("00:02:21");
        portion.setOutputPath("./MsnpHI8E7ec.mp4");
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
