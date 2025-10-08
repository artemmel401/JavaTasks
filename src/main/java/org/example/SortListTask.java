package org.example;

import java.util.ArrayList;
import java.util.Random;

//task 2

class SortListTask {
    final private ArrayList<Double> list;
    final private Random random;

    public SortListTask(int size) {
        list = new ArrayList<>();
        random = new Random();
        fillList(size);
        processList();
    }

    private void fillList(int size) {
        for (int i = 0; i < size; i++) {
            // Генерируем числа от 0.0 до 100.0 с двумя знаками после запятой
            double value = Math.round(random.nextDouble() * 100 * 100.0) / 100.0;
            list.add(value);
        }
    }

    private ArrayList<Double> selectionSort() {
        ArrayList<Double> sortedList = new ArrayList<>(list);
        int n = sortedList.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j) < list.get(minIndex)) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                double temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
        return sortedList;
    }

    private void processList() {
        System.out.println("Исходный список: " + list);
        selectionSort();

        System.out.println("\nОтсортированный список: " + selectionSort());
    }
}
