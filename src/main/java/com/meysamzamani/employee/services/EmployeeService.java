package com.meysamzamani.employee.services;

import com.meysamzamani.employee.domain.Employee;
import com.meysamzamani.employee.exceptions.NotFoundException;
import com.meysamzamani.employee.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        if (hasDuplicateHobbies(employee.getHobbies())) {
            throw new IllegalArgumentException("Duplicate hobbies are not allowed.");
        }
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(UUID employeeId) {
        boolean exists = employeeRepository.existsById(employeeId);
        if (!exists) {
            throw new NotFoundException("Employee with UUID " + employeeId.toString() + " does not exist");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Transactional
    public void updateEmployee(UUID employeeId, String firstName, String lastName, String email, String birthDate) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee with ID " + employeeId.toString() + " does not exist"));
        if (firstName != null && !firstName.isEmpty() && !Objects.equals(employee.getFirstName(), firstName)) {
            employee.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty() && !Objects.equals(employee.getLastName(), lastName)) {
            employee.setLastName(lastName);
        }
        if (email != null && !email.isEmpty() && !Objects.equals(employee.getEmail(), email)) {
            Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmail(employee.getEmail());
            if (employeeOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            employee.setEmail(email);
        }
    }

    private boolean hasDuplicateHobbies(List<String> hobbies) {
        Set<String> uniqueHobbies = new HashSet<>(hobbies);
        return uniqueHobbies.size() < hobbies.size();
    }
}
