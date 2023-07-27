package com.meysamzamani.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Transient
    private int age;

    private String firstName;

    private String lastName;

    @ElementCollection
    @CollectionTable(name = "hobbies", joinColumns = @JoinColumn(name = "employee_id"))
    @Column(name = "hobby")
    private List<String> hobbies;

    public Employee() {}

    public Employee(String email, LocalDate birthDate, String firstName, String lastName, List<String> hobbies) {
        this.email = email;
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hobbies = hobbies;
    }

    public Employee(UUID id, String email, LocalDate birthDate, int age, String firstName, String lastName, List<String> hobbies) {
        this.id = id;
        this.email = email;
        this.birthDate = birthDate;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hobbies = hobbies;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
