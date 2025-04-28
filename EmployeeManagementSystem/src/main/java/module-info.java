module com.example.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.example.employeemanagementsystem.model;
    exports com.example.employeemanagementsystem.exception;

    opens com.example.employeemanagementsystem to javafx.fxml;
    exports com.example.employeemanagementsystem;
}