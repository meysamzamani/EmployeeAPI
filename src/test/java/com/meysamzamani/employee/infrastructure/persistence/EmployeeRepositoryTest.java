package com.meysamzamani.employee.infrastructure.persistence;

import com.meysamzamani.employee.domain.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepositoryUnderTest;

    @AfterEach
    void tearDown() {
        employeeRepositoryUnderTest.deleteAll();
    }

    @Test
    void givenEmployee_whenSaveIntoDatabase_thenShouldFindByEmail() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        String email = "test@TEST.com";
        Employee employee = new Employee(email,
                LocalDate.of(1987,7,20),
                "Meysam",
                "Zamani",
                hobbies);
        employeeRepositoryUnderTest.save(employee);

        Optional<Employee> savedEmployee = employeeRepositoryUnderTest.findEmployeeByEmailIgnoreCase("test@test.com");

        assertAll(
                () -> assertEquals(1, employeeRepositoryUnderTest.findAll().size()),
                () -> assertTrue(savedEmployee.isPresent()),
                () -> assertEquals(email, savedEmployee.get().getEmail())
        );
    }

    @Test
    void givenEmployees_whenSaved_thenShouldReturnTwoEmployees() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("email@email.com",
                LocalDate.of(1900,5,10),
                "NAME",
                "FAMILY",
                hobbies);
        Employee employee2 = new Employee("email2@email.com",
                LocalDate.of(1950,4,11),
                "NAME2",
                "FAMILY2",
                null);
        employeeRepositoryUnderTest.save(employee);
        employeeRepositoryUnderTest.save(employee2);

        List<Employee> savedEmployees = employeeRepositoryUnderTest.findAll();

        assertAll(
                () -> assertNotNull(savedEmployees),
                () -> assertEquals(2, savedEmployees.size())
        );
    }

    @Test
    void givenEmployee_whenSaved_thenFindByUUID() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        String email = "test@test.com";
        Employee employee = new Employee(email,
                LocalDate.of(1900,10,9),
                "NAME",
                "FAMILY",
                hobbies);
        Employee savedEmployee = employeeRepositoryUnderTest.save(employee);

        Optional<Employee> findedEmployee = employeeRepositoryUnderTest.findById(savedEmployee.getId());

        assertAll(
                () -> assertEquals(1, employeeRepositoryUnderTest.findAll().size()),
                () -> assertTrue(findedEmployee.isPresent()),
                () -> assertEquals(email, findedEmployee.get().getEmail()),
                () -> assertEquals(savedEmployee.getId(), findedEmployee.get().getId())
        );
    }

    @Test
    void givenEmployee_whenDeleteById_thenNotExist() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        String email = "test@test.com";
        Employee employee = new Employee(email,
                LocalDate.of(1900,10,9),
                "NAME",
                "FAMILY",
                hobbies);
        Employee savedEmployee = employeeRepositoryUnderTest.save(employee);
        UUID id = savedEmployee.getId();
        assertEquals(1, employeeRepositoryUnderTest.findAll().size());

        employeeRepositoryUnderTest.deleteById(id);
        assertEquals(0, employeeRepositoryUnderTest.findAll().size());

        Optional<Employee> employeeById = employeeRepositoryUnderTest.findById(id);
        assertFalse(employeeById.isPresent());
    }
}