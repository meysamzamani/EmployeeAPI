package com.meysamzamani.employee.services;

import com.meysamzamani.employee.domain.Employee;
import com.meysamzamani.employee.dto.EmployeeUpdateDTO;
import com.meysamzamani.employee.exceptions.NotFoundException;
import com.meysamzamani.employee.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmailIgnoreCase(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("Email " + employee.getEmail() + " is already associated with another employee.");
        }
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID employeeId) {
        boolean exists = employeeRepository.existsById(employeeId);
        if (exists) {
            employeeRepository.deleteById(employeeId);
            return;
        }
        throw new NotFoundException("Employee with ID " + employeeId + " not found.");
    }

    public Employee updateEmployee(UUID employeeId, EmployeeUpdateDTO employeeUpdateDTO) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {

            if (employeeUpdateDTO.getEmail() != null) {

                Optional<Employee> employeeByEmail = employeeRepository.findEmployeeByEmailIgnoreCase(employeeUpdateDTO.getEmail());
                if (employeeByEmail.isPresent() && !employeeByEmail.get().getId().equals(employeeId)) {
                    throw new IllegalStateException("Email " + employeeUpdateDTO.getEmail() + " is already associated with another employee.");
                }
                employee.get().setEmail(employeeUpdateDTO.getEmail());
            }

            if (employeeUpdateDTO.getFirstName() != null) {
                employee.get().setFirstName(employeeUpdateDTO.getFirstName());
            }

            if (employeeUpdateDTO.getLastName() != null) {
                employee.get().setLastName(employeeUpdateDTO.getLastName());
            }

            if (employeeUpdateDTO.getBirthDate() != null) {
                employee.get().setBirthDate(employeeUpdateDTO.getBirthDate());
            }

            if (employeeUpdateDTO.getHobbies() != null) {
                employee.get().setHobbies(employeeUpdateDTO.getHobbies());
            }

            return employeeRepository.save(employee.get());
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
