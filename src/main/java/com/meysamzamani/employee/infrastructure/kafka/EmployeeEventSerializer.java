package com.meysamzamani.employee.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class EmployeeEventSerializer implements Serializer<EmployeeEvent> {

    private final ObjectMapper objectMapper;

    public EmployeeEventSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, EmployeeEvent data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing EmployeeEvent for producing: " + e.getMessage(), e);
        }
    }
}
