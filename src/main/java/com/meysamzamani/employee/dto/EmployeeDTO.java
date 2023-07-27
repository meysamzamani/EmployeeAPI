package com.meysamzamani.employee.dto;

import com.meysamzamani.employee.domain.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class EmployeeDTO {

    private UUID id;
    private String fullName;
    private String email;
    private List<String> hobbies;
    private LocalDate birthDate;
    private int age;

    private void setId(UUID id) {
        this.id = id;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    private void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    private void setAge(int age) {
        this.age = age;
    }

    private UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return age;
    }

    public static EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        String fullName = employee.getLastName().isEmpty() ? employee.getFirstName() : employee.getFirstName() + " " + employee.getLastName();
        employeeDTO.setFullName(fullName);
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setHobbies(employee.getHobbies());
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setAge(employee.getAge());
        return employeeDTO;
    }

    public static Employee convertToEntity(EmployeeDTO employeeDTO) {

        String firstName = "";
        String lastName = "";
        String fullName = employeeDTO.getFullName() == null ? "" : employeeDTO.getFullName();
        if (fullName.split("\\w+").length>1){
            lastName = fullName.substring(fullName.lastIndexOf(" ")+1);
            firstName = fullName.substring(0, fullName.lastIndexOf(" "));
        } else {
            firstName = fullName;
        }

        return new Employee(
                employeeDTO.getId(),
                employeeDTO.getEmail(),
                employeeDTO.getBirthDate(),
                employeeDTO.getAge(),
                firstName,
                lastName,
                employeeDTO.getHobbies()
        );
    }
}
