package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Employee {
    private String fullName;
    private Integer age;
    private String department;
    private Double salary;

    public Employee(String fullName, Integer age, String department, Double salary) {
        this.fullName = fullName;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }
    public String getFullName() {
        return fullName;
    }

    public Integer getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public Double getSalary() {
        return salary;
    }

    // Сеттеры
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return String.format("Сотрудник: %s, Возраст: %d, Отдел: %s, З/П: %.2f",
                fullName, age, department, salary);
    }
    // Метод для извлечения имени из ФИО
    public String getFirstName() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] names = fullName.split(" ");
        return names.length > 0 ? names[0] : "";
    }
    public static ArrayList<Employee> createEmployeeList() {
        ArrayList<Employee> employees = new ArrayList<>();

        employees.add(new Employee("Иванов Иван Иванович", 32, "IT", 85000.0));
        employees.add(new Employee("Петрова Анна Сергеевна", 28, "HR", 65000.0));
        employees.add(new Employee("Сидоров Алексей Петрович", 45, "Бухгалтерия", 95000.0));
        employees.add(new Employee("Козлова Мария Владимировна", 35, "IT", 90000.0));
        employees.add(new Employee("Васильев Дмитрий Олегович", 29, "Маркетинг", 75000.0));
        employees.add(new Employee("Николаева Екатерина Игоревна", 41, "HR", 80000.0));
        employees.add(new Employee("Федоров Сергей Александрович", 38, "IT", 110000.0));

        return employees;
    }
    public static List<String> convertToNameDepartmentList(List<Employee> employees) {
        return employees.stream()
                .map(employee -> employee.getFirstName() + " - " + employee.getDepartment())
                .collect(Collectors.toList());
    }
}
