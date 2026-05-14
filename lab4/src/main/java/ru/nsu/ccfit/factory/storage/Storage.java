package ru.nsu.ccfit.factory.storage;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T> {
    private final int capacity;
    private final Queue<T> items;
    private int totalProduced = 0;

    public Storage(int capacity) {
        this.capacity = capacity;
        this.items = new LinkedList<>();
    }

    public synchronized void put(T item) throws InterruptedException {
        while (items.size() >= capacity) {
            wait();
        }
        items.add(item);
        totalProduced++;
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (items.isEmpty()) {
            wait();
        }
        T item = items.poll();
        notifyAll();
        return item;
    }

    public synchronized int getCount() {
        return items.size();
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }
}
