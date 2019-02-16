package com.musingscafe.tube.extractor;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.musingscafe.tube.extractor.commands.ShellCommand;
import com.musingscafe.tube.extractor.events.LifeCycleEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ShellCommandExecutor {
    private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ExecutorService taskService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final EventBus shellEventBus;

    public ShellCommandExecutor(EventBus shellEventBus) {
        this.shellEventBus = shellEventBus;
        this.shellEventBus.register(this);
    }

    public void submit(ShellCommand shellCommand) {
        taskService.submit(() -> execute(shellCommand));
        System.out.println(String.format("Submitted %s", shellCommand.getCommandName()));
    }

    public void execute(ShellCommand shellCommand) {
        final StringBuffer outBuffer = new StringBuffer();
        final StringBuffer errBuffer = new StringBuffer();
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", shellCommand.prepare());

        Process process = null;
        int exitCode = 0;
        try {
            process = processBuilder.start();

            final InputStream inputStream = process.getInputStream();
            final InputStream errStream = process.getErrorStream();

            final Future inFuture = service.submit(new StreamGobbler(inputStream, outBuffer));
            final Future errFuture = service.submit(new StreamGobbler(errStream, errBuffer));

            exitCode = process.waitFor();

            inFuture.get();
            errFuture.get();

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        final ShellResponse response = new ShellResponse();
        response.setCommandName(shellCommand.getCommandName());
        response.setCommandGroup(shellCommand.getCommandGroup());
        response.setResponse(outBuffer.toString());
        shellEventBus.post(response);
    }

    @Subscribe public void onShutDown(ShellResponse shellResponse) {
        service.shutdown();
        taskService.shutdown();
    }
}
