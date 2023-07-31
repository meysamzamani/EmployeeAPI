package com.meysamzamani.employee.application;

import com.meysamzamani.employee.domain.Employee;
import com.meysamzamani.employee.infrastructure.kafka.EmployeeEvent;
import com.meysamzamani.employee.presentation.dto.EmployeeUpdateDTO;
import com.meysamzamani.employee.infrastructure.kafka.EventType;
import com.meysamzamani.employee.presentation.exceptions.NotFoundException;
import com.meysamzamani.employee.infrastructure.kafka.KafkaEmployeeProducer;
import com.meysamzamani.employee.infrastructure.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final KafkaEmployeeProducer kafkaEmployeeProducer;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           KafkaEmployeeProducer employeeProducer) {
        this.employeeRepository = employeeRepository;
        this.kafkaEmployeeProducer = employeeProducer;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmailIgnoreCase(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("Email " + employee.getEmail() + " is already associated with another employee.");
        }

        Employee registeredEmployee = employeeRepository.save(employee);
        EmployeeEvent employeeEvent = new EmployeeEvent(EventType.CREATE, registeredEmployee);
        kafkaEmployeeProducer.produceEmployee(employeeEvent);
        return registeredEmployee;
    }

    public void deleteEmployee(UUID employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            EmployeeEvent employeeEvent = new EmployeeEvent(EventType.DELETE, employee.get());
            employeeRepository.deleteById(employeeId);
            kafkaEmployeeProducer.produceEmployee(employeeEvent);
            return;
        }
        throw new NotFoundException("Employee with ID " + employeeId + " not found.");
    }

    public Employee updateEmployee(UUID employeeId, EmployeeUpdateDTO employeeUpdateDTO) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee existEmployee = employee.get();

            if (employeeUpdateDTO.getEmail() != null) {

                Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmailIgnoreCase(employeeUpdateDTO.getEmail());
                if (employeeByEmail.isPresent() && !employeeByEmail.get().getId().equals(employeeId)) {
                    throw new IllegalStateException("Email " + employeeUpdateDTO.getEmail() + " is already associated with another employee.");
                }
                existEmployee.setEmail(employeeUpdateDTO.getEmail());
            }

            if (employeeUpdateDTO.getFirstName() != null) {
                existEmployee.setFirstName(employeeUpdateDTO.getFirstName());
            }

            if (employeeUpdateDTO.getLastName() != null) {
                existEmployee.setLastName(employeeUpdateDTO.getLastName());
            }

            if (employeeUpdateDTO.getBirthDate() != null) {
                existEmployee.setBirthDate(employeeUpdateDTO.getBirthDate());
            }

            if (employeeUpdateDTO.getHobbies() != null) {
                existEmployee.setHobbies(employeeUpdateDTO.getHobbies());
            }

            Employee updatedEmployee =  employeeRepository.save(existEmployee);
            EmployeeEvent employeeEvent = new EmployeeEvent(EventType.UPDATE, updatedEmployee);
            kafkaEmployeeProducer.produceEmployee(employeeEvent);
            return updatedEmployee;
        }

        throw new NotFoundException("Employee with ID " + employeeId + " not found.");

    }

    public Employee getEmployee(UUID employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        throw new NotFoundException("Employee with ID " + employeeId + " not found.");
    }

}
