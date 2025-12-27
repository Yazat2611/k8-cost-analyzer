package in.k8s.costanalyzer.service;

import in.k8s.costanalyzer.entity.PodEntity;
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
            log.info("=== SENDING TO KAFKA ===");
            log.info("Topic: {}", topic);
            log.info("Key: {}", entity.getPodName());
            log.info("Entity: {}", entity);

            var future = kafkaTemplate.send(topic, entity.getPodName(), entity);
            var result = future.get(5, TimeUnit.SECONDS);

            log.info("=== SUCCESS ===");
            log.info("Partition: {}", result.getRecordMetadata().partition());
            log.info("Offset: {}", result.getRecordMetadata().offset());
            log.info("Topic: {}", result.getRecordMetadata().topic());

        } catch (Exception e) {
            log.error("=== KAFKA SEND FAILED ===", e);
            throw new RuntimeException(e);
        }
    }
}