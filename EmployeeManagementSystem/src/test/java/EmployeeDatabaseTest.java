import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.EmployeeDatabase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDatabaseTest {

    public EmployeeDatabase db;

    @BeforeEach
    public void setUp() {
        EmployeeDatabase db = new EmployeeDatabase();
    }

    @Test
    public void testAddEmployeeSuccessfully() throws Exception {
        Employee emp = new Employee(1, "Alice",
                "HR", 5000, 4.5, 1, true);
        db.addEmployee(emp);
        assertEquals("Alice", db.getEmployee(1).getName());
    }

    @Test
    public void testInvalidSalary() {
        assertThrows(InvalidSalaryException.class, () -> {
            db.addEmployee(new Employee(2, "Bob",
                    "HR", -1000, 8, 2, false));
        });
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        db.addEmployee(new Employee(3, "Tom", "IT", 7000));
        db.deleteEmployee(3);
        assertThrows(EmployeeNotFoundException.class, () -> db.getEmployee(3));
    }

    @Test
    public void testDepartmentSearch() throws Exception {
        db.addEmployee(new Employee(4, "Sara", "IT", 6000));
        assertTrue(db.getEmployeesByDepartment("IT").stream()
                .anyMatch(emp -> emp.getName().equals("Sara")));
    }
}
