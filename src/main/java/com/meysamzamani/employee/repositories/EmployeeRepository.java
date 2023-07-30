package com.meysamzamani.employee.repositories;

import com.meysamzamani.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findEmployeeByEmailIgnoreCase(String email);
    List<Employee> findAll();
    Employee save(Employee employee);
    void deleteById(UUID id);

}
