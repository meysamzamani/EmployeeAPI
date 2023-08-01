package com.meysamzamani.employee.presentation.controller;

import com.meysamzamani.employee.domain.Employee;
import com.meysamzamani.employee.infrastructure.persistence.EmployeeRepository;
import com.meysamzamani.employee.presentation.dto.EmployeeUpdateDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092" })
class EmployeeControllerIntegrationTest {

    @LocalServerPort
    int port;

    String baseUrl = "http://localhost";

    static RestTemplate restTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeAll
    static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        baseUrl = baseUrl.concat(":").concat(port+"").concat("/api/v1.0/employee");
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployee_whenGetCall_thenListEmployeeAndEqualOne() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@test.com",
                LocalDate.of(1987,3,22),
                "Meysam",
                "Zamani",
                hobbies);
        employeeRepository.save(employee);

        List<Employee> employees = restTemplate.getForObject(baseUrl,List.class);

        assertAll(() -> assertNotNull(employees),
                () -> assertEquals(1, employees.size()),
                () -> assertEquals(1, employeeRepository.findAll().size()));
    }

    @Test
    void givenEmployee_whenGetCallByUuid_thenEmployeeAndEqualUuid() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@test.com",
                LocalDate.of(1987,3,22),
                "Meysam",
                "Zamani",
                hobbies);
        Employee savedEmployee = employeeRepository.save(employee);

        Employee response = restTemplate.getForObject(baseUrl+"/{id}", Employee.class, savedEmployee.getId());

        assertAll(() -> assertNotNull(response),
                () -> assertEquals(savedEmployee.getId(), response.getId()),
                () -> assertEquals("test@test.com", response.getEmail()),
                () -> assertEquals("Meysam", response.getFirstName()),
                () -> assertEquals("Zamani", response.getLastName()),
                () -> assertEquals("Meysam Zamani", response.getFullName()),
                () -> assertEquals(1, employeeRepository.findAll().size()));

    }

    @Test
    void givenEmployee_whenPostCall_thenShouldExistAndEqual() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@test.com",
                LocalDate.of(1987,3,22),
                "Meysam",
                "Zamani",
                hobbies);
        Employee response = restTemplate.postForObject(baseUrl, employee, Employee.class);

        assertAll(() -> assertNotNull(response),
                  () -> assertNotNull(response.getId()),
                  () -> assertEquals("test@test.com", response.getEmail()),
                  () -> assertEquals("Meysam", response.getFirstName()),
                  () -> assertEquals("Zamani", response.getLastName()),
                  () -> assertEquals("Meysam Zamani", response.getFullName()),
                  () -> assertEquals(1, employeeRepository.findAll().size()));
    }

    @Test
    void givenEmployee_whenDeleteCallByUuid_thenNotExist() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@test.com",
                LocalDate.of(1987,3,22),
                "Meysam",
                "Zamani",
                hobbies);
        Employee savedEmployee = employeeRepository.save(employee);

        assertEquals(1, employeeRepository.findAll().size());
        assertTrue(employeeRepository.findById(savedEmployee.getId()).isPresent());

        restTemplate.delete(baseUrl+"/{id}", savedEmployee.getId());

        assertAll(() -> assertFalse(employeeRepository.findById(savedEmployee.getId()).isPresent()),
                () -> assertEquals(0, employeeRepository.findAll().size()));
    }

    @Test
    void givenEmployee_whenPutCallByUuid_thenShouldMatch() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@test.com",
                LocalDate.of(1987,3,22),
                "Meysam",
                "Zamani",
                hobbies);
        Employee savedEmployee = employeeRepository.save(employee);

        assertAll(() -> assertNotNull(savedEmployee),
                () -> assertEquals("test@test.com", savedEmployee.getEmail()),
                () -> assertEquals("Meysam", savedEmployee.getFirstName()),
                () -> assertEquals("Zamani", savedEmployee.getLastName()),
                () -> assertEquals("Meysam Zamani", savedEmployee.getFullName()),
                () -> assertEquals(1, employeeRepository.findAll().size()));

        EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
        updateDTO.setEmail("test2@test.com");

        restTemplate.put(baseUrl+"/{id}", updateDTO, savedEmployee.getId());

        Optional<Employee> updatedEmployee = employeeRepository.findById(savedEmployee.getId());

        assertAll(() -> assertNotNull(updatedEmployee),
                () -> assertTrue(updatedEmployee.isPresent()),
                () -> assertEquals(savedEmployee.getId(), updatedEmployee.get().getId()),
                () -> assertEquals("test2@test.com", updatedEmployee.get().getEmail()),
                () -> assertEquals("Meysam", updatedEmployee.get().getFirstName()),
                () -> assertEquals("Zamani", updatedEmployee.get().getLastName()),
                () -> assertEquals("Meysam Zamani", updatedEmployee.get().getFullName()),
                () -> assertEquals(1, employeeRepository.findAll().size()));
    }
}