package ru.nsu.ccfit.factory.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Body extends Detail {
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public Body() {
        this.id = idCounter.getAndIncrement();
    }
}
