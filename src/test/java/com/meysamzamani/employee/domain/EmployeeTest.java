package com.meysamzamani.employee.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setup() {
        employee = new Employee();
    }

    @Test
    void givenProperties_whenSet_thenShouldEquals() {

        UUID id = UUID.randomUUID();
        String email = "test@test.com";
        String firstName = "Meysam";
        String lastName = "Zamani";
        LocalDate birthDate = LocalDate.of(1987,5,20);
        String[] hobbies = {"Soccer", "Music"};

        employee.setEmail(email);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setBirthDate(birthDate);
        employee.setHobbies(Arrays.asList(hobbies));

        assertEquals(email, employee.getEmail());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(birthDate, employee.getBirthDate());
        assertEquals(Arrays.asList(hobbies), employee.getHobbies());
    }

    @Test
    void givenProperties_whenToString_thenShouldEqualExpected() {

        String email = "test@test.com";
        String firstName = "Meysam";
        String lastName = "Zamani";

        employee.setEmail(email);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);

        String expectedString = "Employee{" +
                "id=null, " +
                "email='test@test.com', " +
                "birthDate=null, " +
                "age=0', " +
                "firstName='Meysam', " +
                "lastName='Zamani', " +
                "fullName='null', " +
                "hobbies=null" +
                "}";

        assertEquals(expectedString, employee.toString());
    }

    @Test
    void givenEmployees_whenIsSame_thenShouldEquals() {
        List<String> hobbies = Arrays.asList("soccer", "music");

        Employee employee1 = new Employee("test1@example.com", LocalDate.of(1987, 5, 20), "Meysam", "Zamani", hobbies);
        Employee employee2 = new Employee("test1@example.com", LocalDate.of(1987, 5, 20), "Meysam", "Zamani", hobbies);

        assertThat(employee1).usingRecursiveComparison().isEqualTo(employee2);
    }
}
