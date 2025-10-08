package org.example;

import java.util.Queue;
import java.util.LinkedList;

// Интерфейс Task
interface Task {
    void start();
    void stop();
    void addData(String data);
}

public class DataProcessor implements Task {
    final private Queue<String> queue;
    private volatile boolean running;
    private Thread processingThread;

    public DataProcessor() {
        this.queue = new LinkedList<>();
        this.running = false;
    }

    @Override
    public void start() {
        if (running) {
            System.out.println("Обработка уже запущена");
            return;
        }

        running = true;
        processingThread = new Thread(this::processData);
        processingThread.start();
        System.out.println("Запущена обработка данных из очереди");
    }

    @Override
    public void stop() {
        if (!running) {
            System.out.println("Обработка уже остановлена");
            return;
        }

        running = false;
        if (processingThread != null) {
            processingThread.interrupt();
        }
        System.out.println("Обработка данных остановлена");
    }
    public void addData(String data) {
        synchronized (queue) {
            queue.offer(data);
            System.out.println("Добавлены данные: " + data);
        }
    }

    private void processData() {
        while (running) {
            try {
                String data = null;

                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        data = queue.poll();
                    }
                }

                if (data != null) {
                    System.out.println("Обработка...: " + data);
                    Thread.sleep(1000);
                    System.out.println("Обработка завершена: " + data);
                } else {
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
