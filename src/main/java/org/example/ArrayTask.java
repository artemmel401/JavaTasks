package org.example;
import java.util.Random;

//task 1

public class ArrayTask {
    final private int[] array;
    final private Random random;

    public ArrayTask(int size) {
        array = new int[size];
        random = new Random();
        fillArray();
        processArray();
    }
    private void fillArray() {
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(201) - 100; // числа от -100 до 100
        }
    }
    private void processArray() {
        System.out.print("Массив: ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        Integer lastPositive = null;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] > 0) {
                lastPositive = array[i];
                break;
            }
        }
        if (lastPositive != null) {
            System.out.println("Последний положительный элемент: " + lastPositive);
        } else {
            System.out.println("В массиве нет положительных элементов");
        }
    }
}
