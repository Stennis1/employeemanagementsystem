package com.example.employeemanagementsystem.controller;
import com.example.employeemanagementsystem.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeController<T> {

    private final Map<T, Employee<T>> employeeMap;

    public EmployeeController() {
        this.employeeMap = new HashMap<>();
    }

    // Add a new employee
    public void addEmployee(Employee<T> employee) {
        employeeMap.put(employee.getEmployeeId(), employee);
    }

    // Remove employee by ID
    public void removeEmployee(T employeeId) {
        employeeMap.remove(employeeId);
    }

    // Update any employee field dynamically
    public void updateEmployeeDetails(T employeeId, String field, Object newValue) {
        Employee<T> employee = employeeMap.get(employeeId);
        if (employee != null) {
            switch (field.toLowerCase()) {
                case "name" -> employee.setName((String) newValue);
                case "department" -> employee.setDepartment((String) newValue);
                case "salary" -> employee.setSalary((Double) newValue);
                case "performancerating" -> employee.setPerformanceRating((Double) newValue);
                case "yearsofexperience" -> employee.setYearsOfExperience((Integer) newValue);
                case "isactive" -> employee.setActive((Boolean) newValue);
                default -> System.out.println("Invalid field: " + field);
            }
        }
    }

    // Return all employees
    public List<Employee<T>> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    public List<Employee<T>> searchByDepartment(String department) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> searchByName(String namePart) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(namePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> filterByPerformance(double minRating) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= minRating)
                .collect(Collectors.toList());
    }

    public List<Employee<T>> filterBySalaryRange(double min, double max) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getSalary() >= min && emp.getSalary() <= max)
                .collect(Collectors.toList());
    }

    public List<Employee<T>> sortBySalary() {
        return employeeMap.values().stream()
                .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> sortByPerformance() {
        return employeeMap.values().stream()
                .sorted((a, b) -> Double.compare(b.getPerformanceRating(), a.getPerformanceRating()))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> sortByExperience() {
        return employeeMap.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Employee<T>> getTop5HighestPaid() {
        return employeeMap.values().stream()
                .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public void giveRaiseToHighPerformers(double minRating, double raisePercent) {
        for (Employee<T> emp : employeeMap.values()) {
            if (emp.getPerformanceRating() >= minRating) {
                double newSalary = emp.getSalary() * (1 + raisePercent / 100);
                emp.setSalary(newSalary);
            }
        }
    }

    public double averageSalaryByDepartment(String department) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }
}
