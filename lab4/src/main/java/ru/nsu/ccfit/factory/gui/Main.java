package ru.nsu.ccfit.factory.gui;

import ru.nsu.ccfit.factory.model.Accessory;
import ru.nsu.ccfit.factory.model.Body;
import ru.nsu.ccfit.factory.model.Car;
import ru.nsu.ccfit.factory.model.Engine;
import ru.nsu.ccfit.factory.storage.Storage;
import ru.nsu.ccfit.factory.utils.Config;
import ru.nsu.ccfit.factory.utils.Logger;
import ru.nsu.ccfit.factory.workers.Dealer;
import ru.nsu.ccfit.factory.workers.Supplier;
import ru.nsu.ccfit.factory.workers.WarehouseController;
import ru.nsu.ccfit.threadpool.ThreadPool;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Config config = new Config("config.properties");

        Storage<Body> bodyStorage = new Storage<>(config.getInt("StorageBodySize"));
        Storage<Engine> engineStorage = new Storage<>(config.getInt("StorageMotorSize"));
        Storage<Accessory> accessoryStorage = new Storage<>(config.getInt("StorageAccessorySize"));
        Storage<Car> carStorage = new Storage<>(config.getInt("StorageAutoSize"));

        Logger logger = new Logger(config.getBoolean("LogSale"));
        ThreadPool threadPool = new ThreadPool(config.getInt("Workers"));

        WarehouseController controller = new WarehouseController(
                carStorage, config.getInt("StorageAutoSize"), threadPool,
                bodyStorage, engineStorage, accessoryStorage
        );

        Supplier<Body> bodySupplier = new Supplier<>(bodyStorage, 1000, Body::new);
        Supplier<Engine> engineSupplier = new Supplier<>(engineStorage, 1000, Engine::new);
        int accSuppliersCount = config.getInt("AccessorySuppliers");
        List<Supplier<Accessory>> accessorySuppliers = new ArrayList<>();
        for (int i = 0; i < accSuppliersCount; i++) {
            accessorySuppliers.add(new Supplier<>(accessoryStorage, 1000, Accessory::new));
        }

        int dealersCount = config.getInt("Dealers");
        List<Dealer> dealers = new ArrayList<>();
        for (int i = 0; i < dealersCount; i++) {
            dealers.add(new Dealer(i + 1, carStorage, 1000, logger));
        }

        FactoryUI ui = new FactoryUI(bodyStorage, engineStorage, accessoryStorage, carStorage, threadPool);
        ui.setupBodySlider(delay -> bodySupplier.setDelay(delay));
        ui.setupEngineSlider(delay -> engineSupplier.setDelay(delay));
        ui.setupAccessorySlider(delay -> {
            for (Supplier<Accessory> supplier : accessorySuppliers) {
                supplier.setDelay(delay);
            }
        });
        ui.setupDealerSlider(delay -> {
            for (Dealer dealer : dealers) {
                dealer.setDelay(delay);
            }
        });

        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("остановка фабрики...");
                bodySupplier.interrupt();
                engineSupplier.interrupt();
                for (Supplier<Accessory> supplier : accessorySuppliers) supplier.interrupt();
                for (Dealer dealer : dealers) dealer.interrupt();
                controller.interrupt();
                threadPool.shutdown();
                logger.close();
                System.out.println("фабрика остановлена.");
            }
        });

        controller.start();
        bodySupplier.start();
        engineSupplier.start();
        for (Supplier<Accessory> supplier : accessorySuppliers) supplier.start();
        for (Dealer dealer : dealers) dealer.start();

        ui.setVisible(true);
    }
}
