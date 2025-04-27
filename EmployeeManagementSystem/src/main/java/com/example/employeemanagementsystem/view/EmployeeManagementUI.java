package com.example.employeemanagementsystem.view;
import com.example.employeemanagementsystem.controller.EmployeeController;
import com.example.employeemanagementsystem.model.Employee;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.UUID;

public class EmployeeManagementUI extends VBox {

    private final EmployeeController<UUID> controller = new EmployeeController<>();
    private final TableView<Employee<UUID>> tableView = new TableView<>();
    private final ObservableList<Employee<UUID>> data = FXCollections.observableArrayList();

    public EmployeeManagementUI() {
        setPadding(new Insets(15));
        setSpacing(10);

        setupTableView();
        getChildren().addAll(tableView, createForm(), createButtonBar(), createSearchSortPanel());
    }

    private void setupTableView() {
        TableColumn<Employee<UUID>, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getEmployeeId().toString()));

        TableColumn<Employee<UUID>, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Employee<UUID>, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDepartment()));

        TableColumn<Employee<UUID>, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("%.2f", cell.getValue().getSalary())));

        TableColumn<Employee<UUID>, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getPerformanceRating())));

        TableColumn<Employee<UUID>, String> expCol = new TableColumn<>("Experience");
        expCol.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getYearsOfExperience())));

        TableColumn<Employee<UUID>, String> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().isActive() ? "Yes" : "No"));

        tableView.getColumns().addAll(idCol, nameCol, deptCol, salaryCol, ratingCol, expCol, activeCol);
        tableView.setItems(data);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private GridPane createForm() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField deptField = new TextField();
        TextField salaryField = new TextField();
        TextField ratingField = new TextField();
        TextField expField = new TextField();
        CheckBox activeBox = new CheckBox("Active");

        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Department:"), deptField);
        form.addRow(2, new Label("Salary:"), salaryField);
        form.addRow(3, new Label("Rating:"), ratingField);
        form.addRow(4, new Label("Experience:"), expField);
        form.add(activeBox, 1, 5);

        Button addBtn = new Button("Add Employee");
        addBtn.setOnAction(e -> {
            try {
                UUID id = UUID.randomUUID();
                String name = nameField.getText();
                String dept = deptField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                double rating = Double.parseDouble(ratingField.getText());
                int experience = Integer.parseInt(expField.getText());
                boolean active = activeBox.isSelected();

                Employee<UUID> emp = new Employee<>(id, name, dept, salary, rating, experience, active);
                controller.addEmployee(emp);
                data.setAll(controller.getAllEmployees());

                nameField.clear();
                deptField.clear();
                salaryField.clear();
                ratingField.clear();
                expField.clear();
                activeBox.setSelected(false);

            } catch (Exception ex) {
                showAlert("Invalid Input", "Please enter valid values for all fields.");
            }
        });

        form.add(addBtn, 1, 6);
        return form;
    }

    private HBox createButtonBar() {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(10));

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            Employee<UUID> selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.removeEmployee(selected.getEmployeeId());
                data.setAll(controller.getAllEmployees());
            }
        });

        Button updateBtn = new Button("Update Selected (Salary +10%)");
        updateBtn.setOnAction(e -> {
            Employee<UUID> selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                double newSalary = selected.getSalary() * 1.10;
                controller.updateEmployeeDetails(selected.getEmployeeId(), "salary", newSalary);
                data.setAll(controller.getAllEmployees());
            }
        });

        Button raiseBtn = new Button("Give Raise to High Performers (≥4.5)");
        raiseBtn.setOnAction(e -> {
            controller.giveRaiseToHighPerformers(4.5, 10);
            data.setAll(controller.getAllEmployees());
        });

        bar.getChildren().addAll(removeBtn, updateBtn, raiseBtn);
        return bar;
    }

    private VBox createSearchSortPanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Name or Department");

        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            String term = searchField.getText().trim();
            if (!term.isEmpty()) {
                List<Employee<UUID>> results = controller.searchByName(term);
                results.addAll(controller.searchByDepartment(term));
                data.setAll(results);
            } else {
                data.setAll(controller.getAllEmployees());
            }
        });

        HBox sortBar = new HBox(10);
        Button sortSalaryBtn = new Button("Sort by Salary");
        Button sortRatingBtn = new Button("Sort by Rating");
        Button sortExpBtn = new Button("Sort by Experience");

        sortSalaryBtn.setOnAction(e -> data.setAll(controller.sortBySalary()));
        sortRatingBtn.setOnAction(e -> data.setAll(controller.sortByPerformance()));
        sortExpBtn.setOnAction(e -> data.setAll(controller.sortByExperience()));

        sortBar.getChildren().addAll(sortSalaryBtn, sortRatingBtn, sortExpBtn);
        vbox.getChildren().addAll(searchField, searchBtn, sortBar);

        return vbox;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

