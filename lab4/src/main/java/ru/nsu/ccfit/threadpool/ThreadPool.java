package ru.nsu.ccfit.threadpool;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private final Queue<Task> taskQueue;
    private final Worker[] workers;
    private boolean isRunning;

    public ThreadPool(int threadCount) {
        this.taskQueue = new LinkedList<>();
        this.workers = new Worker[threadCount];
        this.isRunning = true;

        for (int i = 0; i < threadCount; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public synchronized void addTask(Task task) {
        if (!isRunning) return;
        taskQueue.add(task);
        notify();
    }

    public synchronized void shutdown() {
        isRunning = false;
        notifyAll();
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }

    public synchronized int getPendingTasksCount() {
        return taskQueue.size();
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (isRunning) {
                Task task = null;
                
                synchronized (ThreadPool.this) {
                    while (taskQueue.isEmpty() && isRunning) {
                        try {
                            ThreadPool.this.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    
                    if (!isRunning && taskQueue.isEmpty()) {
                        break;
                    }
                    
                    task = taskQueue.poll();
                }

                if (task != null) {
                    task.execute();
                }
            }
        }
    }
}
