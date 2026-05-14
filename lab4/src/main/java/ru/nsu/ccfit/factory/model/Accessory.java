package ru.nsu.ccfit.factory.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Accessory extends Detail {
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public Accessory() {
        this.id = idCounter.getAndIncrement();
    }
}
