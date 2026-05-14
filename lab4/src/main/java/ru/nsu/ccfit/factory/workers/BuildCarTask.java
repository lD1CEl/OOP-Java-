package ru.nsu.ccfit.factory.workers;

import ru.nsu.ccfit.factory.model.Accessory;
import ru.nsu.ccfit.factory.model.Body;
import ru.nsu.ccfit.factory.model.Car;
import ru.nsu.ccfit.factory.model.Engine;
import ru.nsu.ccfit.factory.storage.Storage;
import ru.nsu.ccfit.threadpool.Task;

public class BuildCarTask implements Task {
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;

    public BuildCarTask(Storage<Body> bodyStorage, Storage<Engine> engineStorage, 
                        Storage<Accessory> accessoryStorage, Storage<Car> carStorage) {
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
    }

    @Override
    public void execute() {
        try {
            Body body = bodyStorage.get();
            Engine engine = engineStorage.get();
            Accessory accessory = accessoryStorage.get();

            Car car = new Car(body, engine, accessory);

            carStorage.put(car);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}