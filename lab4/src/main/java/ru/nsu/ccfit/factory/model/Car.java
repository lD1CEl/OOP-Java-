package ru.nsu.ccfit.factory.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    
    private final int id;
    private final Body body;
    private final Engine engine;
    private final Accessory accessory;

    public Car(Body body, Engine engine, Accessory accessory) {
        this.id = idCounter.getAndIncrement();
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }

    public int getId() {
        return id;
    }

    public String getComponentsInfo() {
        return String.format("Auto %d (Body: %d, Motor: %d, Accessory: %d)",
                id, body.getId(), engine.getId(), accessory.getId());
    }
}
