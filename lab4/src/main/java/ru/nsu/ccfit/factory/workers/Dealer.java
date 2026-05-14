package ru.nsu.ccfit.factory.workers;

import ru.nsu.ccfit.factory.model.Car;
import ru.nsu.ccfit.factory.storage.Storage;
import ru.nsu.ccfit.factory.utils.Logger;

public class Dealer extends Thread {
    private int delay;
    private final Storage<Car> carStorage;
    private final Logger logger;
    private final int dealerNumber;

    public Dealer(int dealerNumber, Storage<Car> carStorage, int initialDelay, Logger logger) {
        this.dealerNumber = dealerNumber;
        this.carStorage = carStorage;
        this.delay = initialDelay;
        this.logger = logger;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Thread.sleep(delay);
                Car car = carStorage.get();
                String message = "Dealer " + dealerNumber + ": " + car.getComponentsInfo();
                logger.log(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
