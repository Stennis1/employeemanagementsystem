package com.example.employeemanagementsystem;
import com.example.employeemanagementsystem.view.EmployeeManagementUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        EmployeeManagementUI ui = new EmployeeManagementUI();
        Scene scene = new Scene(ui, 1050, 900);

        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

