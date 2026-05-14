package ru.nsu.ccfit.factory.workers;

import ru.nsu.ccfit.factory.model.Accessory;
import ru.nsu.ccfit.factory.model.Body;
import ru.nsu.ccfit.factory.model.Car;
import ru.nsu.ccfit.factory.model.Engine;
import ru.nsu.ccfit.factory.storage.Storage;
import ru.nsu.ccfit.threadpool.ThreadPool;

public class WarehouseController extends Thread {
    private final Storage<Car> carStorage;
    private final ThreadPool threadPool;
    
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    
    private final int carStorageCapacity;

    public WarehouseController(Storage<Car> carStorage, int carStorageCapacity, ThreadPool threadPool, 
                               Storage<Body> bodyStorage, Storage<Engine> engineStorage, Storage<Accessory> accessoryStorage) {
        this.carStorage = carStorage;
        this.carStorageCapacity = carStorageCapacity;
        this.threadPool = threadPool;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                synchronized (carStorage) {
                    int carsNeeded = carStorageCapacity - (carStorage.getCount() + threadPool.getPendingTasksCount());
                    
                    if (carsNeeded > 0) {
                        for (int i = 0; i < carsNeeded; i++) {
                            threadPool.addTask(new BuildCarTask(bodyStorage, engineStorage, accessoryStorage, carStorage));
                        }
                    }
                    
                    carStorage.wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
