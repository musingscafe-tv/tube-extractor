package com.musingscafe.tube.extractor;

import com.google.common.eventbus.EventBus;
import com.musingscafe.tube.extractor.commands.ShellCommand;

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
    }

    public void submit(ShellCommand shellCommand, ShellCallback shellCallback) {
        taskService.submit(() -> execute(shellCommand, shellCallback));
        System.out.println(String.format("Submitted %s", shellCommand.getCommandName()));
    }

    public void execute(ShellCommand shellCommand, ShellCallback shellCallback) {
        final StringBuffer outBuffer = new StringBuffer();
        final StringBuffer errBuffer = new StringBuffer();
        final ProcessBuilder processBuilder = new ProcessBuilder();
        System.out.println(shellCommand.prepare());
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
            shellCallback.onError(e);
        }

        final ShellResponse response = new ShellResponse();
        response.setCommandName(shellCommand.getCommandName());
        response.setCommandGroup(shellCommand.getCommandGroup());
        response.setResponse(outBuffer.toString());
//        shellEventBus.post(response);
        if (shellCallback != null) {
            System.out.println("calling back " + shellCommand.getCommandName());
            shellCallback.onComplete(response);
        }
    }

    public void shutDown() {
        service.shutdown();
        taskService.shutdown();
    }
}
