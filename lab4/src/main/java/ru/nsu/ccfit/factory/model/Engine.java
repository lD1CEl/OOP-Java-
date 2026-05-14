package ru.nsu.ccfit.factory.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Engine extends Detail {
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public Engine() {
        this.id = idCounter.getAndIncrement();
    }
}
