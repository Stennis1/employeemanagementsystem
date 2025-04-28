import com.example.employeemanagementsystem.exception.EmployeeNotFoundException;
import com.example.employeemanagementsystem.exception.InvalidSalaryException;
import com.example.employeemanagementsystem.model.Employee;
import com.example.employeemanagementsystem.model.EmployeeDatabase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDatabaseTest {

    public EmployeeDatabase<Integer> db; // Also specify Integer

    @BeforeEach
    public void setUp() {
        db = new EmployeeDatabase<>();
    }

    @Test
    public void testAddEmployeeSuccessfully() throws Exception {
        Employee<Integer> emp = new Employee<>(1, "Alice",
                "HR", 5000, 4.5, 1, true);
        db.addEmployee(emp);
        assertEquals("Alice", db.getEmployee(1).getName());
    }

    @Test
    public void testInvalidSalary() {
        assertThrows(InvalidSalaryException.class, () -> {
            db.addEmployee(new Employee<>(2, "Bob",
                    "HR", -1000, 8.0, 2, false));
        });
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        db.addEmployee(new Employee<>(3, "Tom", "IT",
                7000, 2.5, 1, true));
        db.deleteEmployee(3);
        assertThrows(EmployeeNotFoundException.class, () -> db.getEmployee(3));
    }

    @Test
    public void testDepartmentSearch() throws Exception {
        db.addEmployee(new Employee<>(4, "Sara", "IT",
                6000, 8.0, 3, true));
        assertTrue(db.searchByDepartment("IT").stream()
                .anyMatch(emp -> emp.getName().equals("Sara")));
    }
}
