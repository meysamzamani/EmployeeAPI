package com.meysamzamani.employee.infrastructure.kafka;

import com.meysamzamani.employee.domain.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class KafkaEmployeeProducerTest {

    @Mock
    KafkaTemplate<String, EmployeeEvent> kafkaTemplate;
    @InjectMocks
    KafkaEmployeeProducer kafkaEmployeeProducer;

    @Value("${spring.kafka.topic}")
    String topic;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenProduceEmployee_thenShouldVerifyTopicAndEvent() {
        UUID employeeId = UUID.randomUUID();
        List<String> hobbies = new ArrayList<>();
        hobbies.add("film");
        hobbies.add("photography");
        Employee employee = new Employee(employeeId,"test@yahoo.com",
                LocalDate.of(1987,5,23),
                0,
                "myTestName",
                "myTestFamily",
                "",
                hobbies);

        EmployeeEvent employeeEvent = new EmployeeEvent(EventType.CREATE, employee);

        kafkaEmployeeProducer.produceEmployee(employeeEvent);

        verify(kafkaTemplate, times(1)).send(eq(topic), eq(employeeEvent));
    }
}