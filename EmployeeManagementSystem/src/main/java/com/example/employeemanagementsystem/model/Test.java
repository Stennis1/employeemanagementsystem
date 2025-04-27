package com.example.employeemanagementsystem.model;

import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidDepartmentException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        // Create sample employees
        Employee<Integer> emp1 = new Employee<>(101, "Alice Smith", "IT", 75000, 4.6, 5, true);
        Employee<Integer> emp2 = new Employee<>(102, "Bob Johnson", "HR", 65000, 4.1, 3, true);
        Employee<Integer> emp3 = new Employee<>(103, "Charlie Davis", "Finance", 82000, 4.9, 7, false);

        // Store them in a list
        List<Employee<Integer>> employeeList = new ArrayList<>();
        employeeList.add(emp1);
        employeeList.add(emp2);
        employeeList.add(emp3);

        // Display all employees
        System.out.println("All Employees:");
        for (Employee<Integer> emp : employeeList) {
            System.out.println(emp);
        }

        // Sort by years of experience (uses Comparable)
        Collections.sort(employeeList);

        System.out.println("\nEmployees Sorted by Experience (Descending):");
        for (Employee<Integer> emp : employeeList) {
            System.out.println(emp);
        }

        EmployeeDatabase<Integer> db = new EmployeeDatabase<>();


        // Add employees to database
        try {
            db.addEmployee(emp1);
        } catch (InvalidDepartmentException | InvalidSalaryException e) {
            throw new RuntimeException(e);
        }

        try {
            db.addEmployee(emp2);
        } catch (InvalidDepartmentException | InvalidSalaryException e) {
            throw new RuntimeException(e);
        }

        try {
            db.addEmployee(emp3);
        } catch (InvalidSalaryException | InvalidDepartmentException e) {
            throw new RuntimeException(e);
        }

        // Print all employees
        System.out.println("\nAll Employees:");
        for (Employee<Integer> e : db.getAllEmployees()) {
            System.out.println(e);
        }

        // Update an employee's salary
        try {
            db.updateEmployeeDetails(102, "salary", 70000.0);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Remove an employee
        try {
            db.removeEmployee(103);
        } catch (EmployeeNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Print after update and removal
        System.out.println("\n Updated Employee List:");
        for (Employee<Integer> e : db.getAllEmployees()) {
            System.out.println(e);
        }

        System.out.println("\n Employees in IT Department:");
        for (Employee<Integer> emp : db.searchByDepartment("IT")) {
            System.out.println(emp);
        }

        System.out.println("\n Employees with Name containing 'Ali':");
        for (Employee<Integer> emp : db.searchByName("Ali")) {
            System.out.println(emp);
        }

        System.out.println("\n Employees with Rating ≥ 4.5:");
        for (Employee<Integer> emp : db.filterByPerformance(4.5)) {
            System.out.println(emp);
        }

        System.out.println("\n Employees with Salary between $60,000 and $90,000:");
        for (Employee<Integer> emp : db.filterBySalaryRange(60000, 90000)) {
            System.out.println(emp);
        }

        // Using Iterator
        System.out.println("\n Traversing All Employees using Iterator:");
        Iterator<Employee<Integer>> iterator = db.getEmployeeIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("\n Sorted by Years of Experience (Comparable):");
        List<Employee<Integer>> sortedByExperience = db.getAllEmployees();
        Collections.sort(sortedByExperience);
        for (Employee<Integer> emp : sortedByExperience) {
            System.out.println(emp);
        }

        System.out.println("\n Sorted by Salary (Comparator):");
        List<Employee<Integer>> sortedBySalary = db.getAllEmployees();
        sortedBySalary.sort(new EmployeeSalaryComparator<>());
        for (Employee<Integer> emp : sortedBySalary) {
            System.out.println(emp);
        }

        System.out.println("\n Sorted by Performance Rating (Comparator):");
        List<Employee<Integer>> sortedByPerformance = db.getAllEmployees();
        sortedByPerformance.sort(new EmployeePerformanceComparator<>());
        for (Employee<Integer> emp : sortedByPerformance) {
            System.out.println(emp);
        }

        System.out.println("\n Giving 10% Raise to Employees with Rating ≥ 4.5");
        db.giveRaiseToHighPerformers(4.5, 10);

        System.out.println("\n Top 2 Highest-Paid Employees:");
        List<Employee<Integer>> topPaid = db.getTopPaidEmployees(2);
        for (Employee<Integer> emp : topPaid) {
            System.out.println(emp);
        }

        System.out.println("\n Average Salary in IT Department:");
        double avgSalaryIT = db.getAverageSalaryByDepartment("IT");
        System.out.printf("Average Salary in IT: $%.2f\n", avgSalaryIT);


    }
}
