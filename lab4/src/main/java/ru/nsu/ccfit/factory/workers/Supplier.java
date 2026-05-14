package ru.nsu.ccfit.factory.workers;

import ru.nsu.ccfit.factory.storage.Storage;

public class Supplier<T> extends Thread {
    private int delay;
    private final Storage<T> storage;
    private final java.util.function.Supplier<T> factory;

    public Supplier(Storage<T> storage, int initialDelay, java.util.function.Supplier<T> factory) {
        this.storage = storage;
        this.delay = initialDelay;
        this.factory = factory;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(delay);
                T item = factory.get();
                storage.put(item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
