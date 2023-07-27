package com.meysamzamani.employee.domain;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Employee {

    private UUID uuid;
    private String email;
    private LocalDate birthdate;
    private int age;
    private String firstName;
    private String lastName;
    private String fullName;
    private List<Hobby> hobbies;

}
