package com.example.employeemanagementsystem.model;

import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidDepartmentException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;

import java.util.*;

public class EmployeeDatabase<T> {
    private final Map<T, Employee<T>> employeeMap;

    public EmployeeDatabase() {
        employeeMap = new HashMap<>();
    }

    public void addEmployee(Employee<T> employee) throws InvalidDepartmentException, InvalidSalaryException {
        employeeMap.put(employee.getEmployeeId(), employee);
        System.out.println("Added Employee!");

        if (employee.getSalary() < 0) {
            throw new InvalidSalaryException("Salary cannot be negative!");
        }

        if (employee.getDepartment() == null || employee.getDepartment().isEmpty()) {
            throw new InvalidDepartmentException("Department cannot be empty");
        }
    }

    public void removeEmployee(T employeeId) throws EmployeeNotFoundException {
        if (employeeMap.containsKey(employeeId)) {
            employeeMap.remove(employeeId);
            System.out.println("Removed Employee!");
        } else {
            System.out.println("Employee not found!!");
        }

        if (!employeeMap.containsKey(employeeId)) {
            throw new EmployeeNotFoundException("Employee ID not found!");
        }
    }

    public void updateEmployeeDetails(T employeeId, String field, Object newValue) throws EmployeeNotFoundException {
        Employee<T> employee = employeeMap.get(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + "not found");
        }

        switch (field.toLowerCase()) {
            case "name" -> employee.setName((String) newValue);
            case "department" -> {
                String dept = (String) newValue;
                if (dept == null || dept.isBlank()) {
                try {
                    throw new InvalidDepartmentException("Department cannot be empty");
                } catch (InvalidDepartmentException e) {
                    throw new RuntimeException(e);
                }
                }
                employee.setDepartment(dept);
            }
            case "salary" -> {
                double salary = (Double) newValue;
                if (salary < 0) {
                    try {
                        throw new InvalidSalaryException("Salary must not be negative!");
                    } catch (InvalidSalaryException e) {
                        throw new RuntimeException(e);
                    }
                }
                employee.setSalary(salary);
            }
            case "performancerating" -> employee.setPerformanceRating((double) newValue);
            case "yearsofexperience" -> employee.setYearsOfExperience((Integer) newValue);
            case "isactive" -> employee.setActive((boolean) newValue);
            default -> throw new IllegalArgumentException("Unknown field " + field);
        }
    }

    public List<Employee<T>> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    // Search by Fields
    public List<Employee<T>> searchByDepartment(String department) {
        return employeeMap.values().stream().filter(
                emp -> emp.getDepartment().equalsIgnoreCase(department)).toList();
    }

    public List<Employee<T>> searchByName(String keyword) {
        return employeeMap.values().stream().filter(
                emp -> emp.getName().toLowerCase().contains(keyword.toLowerCase())).toList();
    }

    public List<Employee<T>> filterByPerformance(double minRating) {
        return employeeMap.values().stream().filter(
                emp -> emp.getPerformanceRating() >= minRating).toList();
    }

    public List<Employee<T>> filterBySalaryRange(double minSalary, double maxSalary) {
        return employeeMap.values().stream().filter(
                emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary).toList();
    }

    public Iterator<Employee<T>> getEmployeeIterator() {
        return employeeMap.values().iterator();
    }

    public void giveRaiseToHighPerformers(double minRating, double raisePercent) {
        employeeMap.values().stream().filter(emp -> emp.getPerformanceRating() >= minRating).forEach(
                emp -> {
                    double raiseAmount = emp.getSalary() + (raisePercent / 100);
                    emp.setSalary(emp.getSalary() + raiseAmount);
                }
        );
    }

    public List<Employee<T>> getTopPaidEmployees(int topN){
        return employeeMap.values().stream().sorted(
                Comparator.comparingDouble(Employee::getSalary)).limit(topN).toList();
    }

    public double getAverageSalaryByDepartment(String department) {
        return employeeMap.values().stream().filter(
                emp -> emp.getDepartment().equalsIgnoreCase(department)).mapToDouble(
                        Employee::getSalary).average().orElse(0.0);
    }

    public Employee<T> getEmployee(T employeeId) throws EmployeeNotFoundException {
        Employee<T> employee = employeeMap.get(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " is not found!");
        }
        return employee;
    }

    public void deleteEmployee(T employeeId) throws EmployeeNotFoundException {
        Employee<T> employee = employeeMap.remove(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " is not found!");
        }
    }
}
