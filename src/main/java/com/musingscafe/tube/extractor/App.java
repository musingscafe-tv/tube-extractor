package com.musingscafe.tube.extractor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.musingscafe.tube.extractor.commands.youtube.GetDirectLink;

public class App {
    private boolean shouldWait = true;
    private int counter = 0;
    final EventBus shellEventBus = new EventBus("shellEventBus");
    final EventBus appEventBus = new EventBus("appEventBus");

    public App() {
        shellEventBus.register(this);
    }

    public static void main(String[] args) {
        final App app = new App();
        app.start();
    }

    public void start() {
        final ShellCommandExecutor executor = new ShellCommandExecutor(shellEventBus);
        final GetDirectLink directLink = new GetDirectLink("https://www.youtube.com/watch?v=ZevT5wvFShU");
        final GetDirectLink directLink2 = new GetDirectLink("https://www.youtube.com/watch?v=ZevT5wvFShU");
        executor.submit(directLink);
        executor.submit(directLink2);

        while (shouldWait) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe public void onComplete(ShellResponse shellResponse) {
        System.out.println(String.format("Finished %s", shellResponse.getCommandName()));

        counter++;

        if (counter == 2) {
            shouldWait = false;
        }

    }
}
