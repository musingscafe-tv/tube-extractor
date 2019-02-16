package com.musingscafe.tube.extractor.events;

public class LifeCycleEvent {
    private String event;

    public LifeCycleEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
