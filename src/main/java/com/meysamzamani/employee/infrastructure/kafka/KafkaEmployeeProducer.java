package com.meysamzamani.employee.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEmployeeProducer {

    @Autowired
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topic;

    public void produceEmployee(EmployeeEvent employeeEvent) {
        kafkaTemplate.send(topic, employeeEvent);
        System.out.println(employeeEvent.getEventType() + " | " + employeeEvent.getEmployee().getEmail());
    }
}
