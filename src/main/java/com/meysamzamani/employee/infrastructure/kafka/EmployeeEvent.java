package com.meysamzamani.employee.infrastructure.kafka;

import com.meysamzamani.employee.domain.Employee;

import java.util.ArrayList;

public class EmployeeEvent {

    private EventType eventType;
    private Employee employee;

    public EmployeeEvent(EventType eventType, Employee employee) {
        this.eventType = eventType;
        if (employee.getHobbies() != null) {
            employee.setHobbies(new ArrayList<>(employee.getHobbies()));
        }
        this.employee = employee;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Employee getEmployee() {
        return employee;
    }
}


