package org.example;
import java.util.ArrayList;
import java.util.List;
import static org.example.Employee.convertToNameDepartmentList;
import static org.example.Employee.createEmployeeList;
import static org.example.HttpClientExample.sendHttpRequest;

//task variant 4


public class Main {
    public static void main(String[] args) {
        new ArrayTask(60);
        new SortListTask(10);

        ArrayList<Employee> employees = createEmployeeList();
        System.out.println("Список сотрудников: " + employees);
        List<String> nameDepartmentList = convertToNameDepartmentList(employees);
        System.out.println("Имя + Отдел:");
        for (String item : nameDepartmentList) {
            System.out.println(item);
        }

        try {
            sendHttpRequest("https://httpbin.org/anything");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
        DataProcessor processor = new DataProcessor();
        processor.addData("data 1");
        processor.addData("data 2");
        processor.addData("data 3");
        processor.start();
        try {
            Thread.sleep(2000);
            processor.addData("Данные 4");
            processor.addData("Данные 5");

            Thread.sleep(3000);
            processor.addData("Данные 6");

            Thread.sleep(4000);

        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        processor.stop();
    }
}