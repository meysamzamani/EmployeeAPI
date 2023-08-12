package com.meysamzamani.employee.application;

import com.meysamzamani.employee.domain.Employee;
import com.meysamzamani.employee.infrastructure.kafka.EmployeeEvent;
import com.meysamzamani.employee.infrastructure.kafka.KafkaEmployeeProducer;
import com.meysamzamani.employee.infrastructure.persistence.EmployeeRepository;
import com.meysamzamani.employee.presentation.dto.EmployeeUpdateDTO;
import com.meysamzamani.employee.presentation.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    KafkaEmployeeProducer kafkaEmployeeProducer;
    EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeService(employeeRepository, kafkaEmployeeProducer);
    }

    @Test
    void whenGetEmployees_thenVerifyFindAll() {
        employeeService.getEmployees();
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void whenDuplicateEmail_thenThrowsIllegalStateException() {
        Employee employee = new Employee();
        employee.setEmail("test@test.com");

        when(employeeRepository.findEmployeeByEmailIgnoreCase(anyString())).thenReturn(Optional.of(employee));

        assertThrows(IllegalStateException.class, () -> employeeService.createEmployee(employee));

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(kafkaEmployeeProducer, never()).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void whenCreateEmployee_thenVerifySaveAndProduce() {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee("test@yahoo.com",
                LocalDate.of(1987,5,23),
                "myTestName",
                "myTestFamily",
                hobbies);

        when(employeeRepository.findEmployeeByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.createEmployee(employee);

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(kafkaEmployeeProducer, times(1)).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void whenDeleteEmployee_thenVerifyHierarchy() {
        UUID employeeId = UUID.randomUUID();
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee(employeeId,
               "test@yahoo.com",
                LocalDate.of(1987,5,23),
                0,
                "myTestName",
                "myTestFamily",
                "",
                hobbies);

        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
        verify(kafkaEmployeeProducer, times(1)).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void givenUuid_whenDeleteEmployee_thenThrowsNotFound() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.deleteEmployee(employeeId));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).deleteById(any(UUID.class));
        verify(kafkaEmployeeProducer, never()).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void whenUpdateEmployee_thenVerifySaveAndProduceAndEquals() {
        UUID employeeId = UUID.randomUUID();
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee existingEmployee = new Employee(employeeId,
                "test@yahoo.com",
                LocalDate.of(1987,5,23),
                0,
                "myTestName",
                "myTestFamily",
                "",
                hobbies);

        when(employeeRepository.findById(eq(employeeId))).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findEmployeeByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
        updateDTO.setEmail("updated@yahoo.com");

        Employee updatedEmployee = employeeService.updateEmployee(employeeId, updateDTO);

        assertNotNull(updatedEmployee);
        assertEquals(updateDTO.getEmail(), updatedEmployee.getEmail());

        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(kafkaEmployeeProducer, times(1)).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void givenUuid_whenUpdateEmployee_thenThrowsNotFound() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(eq(employeeId))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
            updateDTO.setEmail("test@test.com");
            employeeService.updateEmployee(employeeId, updateDTO);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
        verify(kafkaEmployeeProducer, never()).produceEmployee(any(EmployeeEvent.class));
    }

    @Test
    void whenGetEmployee_thenVerifyFindById() {
        UUID employeeId = UUID.randomUUID();
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee(employeeId,
                "test@yahoo.com",
                LocalDate.of(1987,5,23),
                0,
                "myTestName",
                "myTestFamily",
                "",
                hobbies);

        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployee(employeeId);

        assertNotNull(result);
        assertEquals(employee, result);

        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void whenGetEmployee_thenThrowsNotFound() {
        UUID employeeId = UUID.randomUUID();

        when(employeeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeService.getEmployee(employeeId));

        verify(employeeRepository, times(1)).findById(employeeId);
    }

}
