package com.example.employeemanagementsystem;
import com.example.employeemanagementsystem.view.EmployeeManagementUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        EmployeeManagementUI ui = new EmployeeManagementUI(); // our custom UI
        Scene scene = new Scene(ui, 950, 600); // set preferred width and height

        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

