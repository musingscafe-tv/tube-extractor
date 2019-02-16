package com.musingscafe.tube.extractor;

public interface ShellCallback {
    void onComplete(ShellResponse shellResponse);
    void onError(Throwable throwable);
}
