package com.musingscafe.tube.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler implements Runnable {
    private final InputStream inputStream;
    private final StringBuffer buffer;

    public StreamGobbler(InputStream inputStream, StringBuffer buffer) {
        this.inputStream = inputStream;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {

            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
