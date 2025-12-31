package in.k8s.costanalyzer.service;

import in.k8s.costanalyzer.entity.PodEntity;
import in.k8s.costanalyzer.exception.KafkaPublishingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, PodEntity> kafkaTemplate;

    @Value("${kafka.topic.resource-metrics}")
    private String topic;

    public void publishMetric(PodEntity entity) {
        try {
            kafkaTemplate.send(topic, entity.getPodName(), entity).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            if(e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Kafka Publishing error", e);
            throw new KafkaPublishingException("Failed to publish metric: " + e.getMessage(), e);
        }
    }
}