import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidDepartmentException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.EmployeeDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeDatabaseTest {

    private EmployeeDatabase<String> database;
    private Employee<String> employee;

    @BeforeEach
    void setUp() {
        database = new EmployeeDatabase<>();
        employee = new Employee<>("EMP001", "John Doe", "Engineering", 50000.0, 4.5, 5, true);
    }

    @Test
    void testAddEmployeeSuccessfully() throws InvalidDepartmentException, InvalidSalaryException {
        database.addEmployee(employee);
        assertEquals(1, database.getAllEmployees().size());
    }

    @Test
    void testAddEmployeeWithNegativeSalary() {
        Employee<String> badEmployee = new Employee<>("EMP002", "Jane Doe", "HR",
                -5000.0, 3.5, 3, true);
        assertThrows(InvalidSalaryException.class, () -> database.addEmployee(badEmployee));
    }

    @Test
    void testAddEmployeeWithEmptyDepartment() {
        Employee<String> badEmployee = new Employee<>("EMP003", "Jake Doe", "",
                40000.0, 3.0, 2, true);
        assertThrows(InvalidDepartmentException.class, () -> database.addEmployee(badEmployee));
    }

    @Test
    void testRemoveEmployeeSuccessfully() throws Exception {
        database.addEmployee(employee);
        database.removeEmployee(employee.getEmployeeId());
        assertEquals(0, database.getAllEmployees().size());
    }

    @Test
    void testRemoveEmployeeThatDoesNotExist() {
        assertThrows(EmployeeNotFoundException.class, () -> database.removeEmployee("NON_EXISTENT"));
    }

    @Test
    void testUpdateEmployeeNameSuccessfully() throws Exception {
        database.addEmployee(employee);
        database.updateEmployeeDetails(employee.getEmployeeId(), "name", "John Smith");
        Employee<String> updated = database.getEmployee(employee.getEmployeeId());
        assertEquals("John Smith", updated.getName());
    }

    @Test
    void testDeleteEmployeeSuccessfully() throws Exception {
        database.addEmployee(employee);
        database.deleteEmployee(employee.getEmployeeId());
        assertEquals(0, database.getAllEmployees().size());
    }

    @Test
    void testDeleteNonExistentEmployee() {
        assertThrows(EmployeeNotFoundException.class, () -> database.deleteEmployee("NON_EXISTENT"));
    }
}
