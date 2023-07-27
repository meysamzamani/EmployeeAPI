package com.meysamzamani.employee.repositories;

import com.meysamzamani.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT e FROM Employee e WHERE e.email = ?1")
    Optional<Employee> findEmployeeByEmail(String email);
    List<Employee> findAll();
    Employee save(Employee employee);
    void deleteById(UUID id);

}
