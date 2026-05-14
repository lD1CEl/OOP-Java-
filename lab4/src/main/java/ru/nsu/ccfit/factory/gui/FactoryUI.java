package ru.nsu.ccfit.factory.gui;

import ru.nsu.ccfit.factory.model.Accessory;
import ru.nsu.ccfit.factory.model.Body;
import ru.nsu.ccfit.factory.model.Car;
import ru.nsu.ccfit.factory.model.Engine;
import ru.nsu.ccfit.factory.storage.Storage;
import ru.nsu.ccfit.threadpool.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FactoryUI extends JFrame {
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    private final ThreadPool threadPool;

    private JLabel bodyLabel;
    private JLabel engineLabel;
    private JLabel accessoryLabel;
    private JLabel carLabel;
    private JLabel taskLabel;

    public FactoryUI(Storage<Body> bodyStorage, Storage<Engine> engineStorage,
                     Storage<Accessory> accessoryStorage, Storage<Car> carStorage,
                     ThreadPool threadPool) {
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.threadPool = threadPool;

        setTitle("автомобильная фабрика");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLayout(new GridLayout(5, 1));

        add(createPanel("кузова (Body)", null));
        add(createPanel("двигатели (Engine)", null));
        add(createPanel("аксессуары (Accessory)", null));
        add(createPanel("машины (Car)", null));

        JPanel taskPanel = new JPanel();
        taskLabel = new JLabel("задач в очереди сборки: 0");
        taskPanel.add(taskLabel);
        add(taskPanel);

        Timer updateTimer = new Timer(100, e -> updateStats());
        updateTimer.start();
    }

    public void setupBodySlider(Consumer<Integer> onChange) {
        replacePanel(0, "кузова (Body)", onChange, bodyLabel = new JLabel());
    }

    public void setupEngineSlider(Consumer<Integer> onChange) {
        replacePanel(1, "двигатели (Engine)", onChange, engineLabel = new JLabel());
    }

    public void setupAccessorySlider(Consumer<Integer> onChange) {
        replacePanel(2, "аксессуары (Accessory)", onChange, accessoryLabel = new JLabel());
    }

    public void setupDealerSlider(Consumer<Integer> onChange) {
        replacePanel(3, "автомобили (Car / Dealers)", onChange, carLabel = new JLabel());
    }

    private void replacePanel(int index, String name, Consumer<Integer> onChange, JLabel label) {
        getContentPane().remove(index);
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(BorderFactory.createTitledBorder(name));
        
        p.add(label);

        JSlider slider = new JSlider(100, 5000, 1000);
        slider.addChangeListener(e -> onChange.accept(slider.getValue()));
        p.add(slider);

        getContentPane().add(p, index);
        revalidate();
    }

    private JPanel createPanel(String name, JLabel label) {
        JPanel p = new JPanel();
        return p;
    }

    private void updateStats() {
        if (bodyLabel != null) {
            bodyLabel.setText(String.format("на складе: %d | всего произведено: %d", 
                    bodyStorage.getCount(), bodyStorage.getTotalProduced()));
        }
        if (engineLabel != null) {
            engineLabel.setText(String.format("на складе: %d | всего произведено: %d", 
                    engineStorage.getCount(), engineStorage.getTotalProduced()));
        }
        if (accessoryLabel != null) {
            accessoryLabel.setText(String.format("на складе: %d | всего произведено: %d", 
                    accessoryStorage.getCount(), accessoryStorage.getTotalProduced()));
        }
        if (carLabel != null) {
            carLabel.setText(String.format("на складе: %d | всего машин собрано: %d", 
                    carStorage.getCount(), carStorage.getTotalProduced()));
        }
        taskLabel.setText("задач в очереди сборки: " + threadPool.getPendingTasksCount());
    }
}
