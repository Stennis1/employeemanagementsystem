package com.example.employeemanagementsystem.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidDepartmentException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;
import java.util.*;

public class EmployeeDatabase<T> {
    private final Map<T, Employee<T>> employeeMap;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDatabase.class);

    public EmployeeDatabase() {
        employeeMap = new HashMap<>();
    }

    public void addEmployee(Employee<T> employee) throws InvalidDepartmentException, InvalidSalaryException {
        if (employee.getSalary() < 0) {
            logger.error("Failed to add employee: Salary is negative for {}", employee.getName());
            throw new InvalidSalaryException("Salary cannot be negative!");
        }

        if (employee.getDepartment() == null || employee.getDepartment().isEmpty()) {
            logger.error("Failed to add employee: Department is empty for {}", employee.getName());
            throw new InvalidDepartmentException("Department cannot be empty");
        }

        employeeMap.put(employee.getEmployeeId(), employee);
        logger.info("Successfully added employee with ID {}", employee.getEmployeeId());
    }

    public void removeEmployee(T employeeId) throws EmployeeNotFoundException {
        if (!employeeMap.containsKey(employeeId)) {
            logger.error("Failed to remove employee: ID {} not found", employeeId);
            throw new EmployeeNotFoundException("Employee ID not found!");
        }
        employeeMap.remove(employeeId);
        logger.info("Successfully removed employee with ID {}", employeeId);
    }

    public void updateEmployeeDetails(T employeeId, String field, Object newValue)
            throws EmployeeNotFoundException, InvalidDepartmentException, InvalidSalaryException {
        Employee<T> employee = employeeMap.get(employeeId);

        if (employee == null) {
            logger.error("Failed to update: Employee with ID {} not found", employeeId);
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + "not found");
        }

        switch (field.toLowerCase()) {
            case "name" -> employee.setName((String) newValue);
            case "department" -> {
                String dept = (String) newValue;
                if (dept == null || dept.isBlank()) {
                    logger.error("Failed to update employee {}: Department is empty", employeeId);
                    throw new InvalidDepartmentException("Department cannot be empty");
                }
            }
            case "salary" -> {
                double salary = (Double) newValue;
                if (salary < 0) {
                    logger.error("Failed to update employee {}: salary is negative", employeeId);
                    throw new InvalidSalaryException("Salary must not be negative!");
                }
            }
            case "performancerating" -> employee.setPerformanceRating((double) newValue);
            case "yearsofexperience" -> employee.setYearsOfExperience((Integer) newValue);
            case "isactive" -> employee.setActive((boolean) newValue);
            default -> {
                logger.error("Unknown field {} provided for update", field);
                throw new IllegalArgumentException("Unknown field " + field);
            }
        }
        logger.info("Successfully updated {} for employee ID {}", field, employeeId);
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
        employeeMap.values().stream().filter(emp ->
                emp.getPerformanceRating() >= minRating).forEach(
                emp -> {
                    double currentSalary = emp.getSalary();
                    double raiseAmount = currentSalary * (raisePercent / 100);
                    emp.setSalary(currentSalary + raiseAmount);
                    logger.info("Gave a raise of {}% to employee {}", raisePercent, emp.getEmployeeId());
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
            logger.error("Failed to retrieve employee: ID {} not found", employeeId);
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " is not found!");
        }
        return employee;
    }

    public void deleteEmployee(T employeeId) throws EmployeeNotFoundException {
        Employee<T> employee = employeeMap.remove(employeeId);
        if (employee == null) {
            logger.error("Failed to delete employee: ID {} not found", employeeId);
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " is not found!");
        }
        logger.info("Successfully deleted employee with ID {}", employeeId);
    }
}
