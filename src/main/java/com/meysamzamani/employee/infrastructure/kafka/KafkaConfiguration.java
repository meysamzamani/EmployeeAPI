package com.meysamzamani.employee.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, EmployeeEvent> getProducerFactory(ObjectMapper objectMapper) {
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new DefaultKafkaProducerFactory<>(map, new StringSerializer(), new EmployeeEventSerializer(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, EmployeeEvent> getKafkaTemplate(ObjectMapper objectMapper) {
        return new KafkaTemplate<>(getProducerFactory(objectMapper));
    }

}
