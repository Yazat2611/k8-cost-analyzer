package in.k8s.costanalyzer.mapper;

import in.k8s.costanalyzer.entity.PodEntity;
import io.kubernetes.client.custom.PodMetrics;
import java.time.LocalDateTime;
import java.util.Objects;

public class PodMapper {
  public static PodEntity convertToEntity(PodMetrics podMetrics) {
    PodEntity entity = new PodEntity();

    entity.setPodName(podMetrics.getMetadata().getName());
    entity.setNamespace(podMetrics.getMetadata().getNamespace());
    double totalCpu =
        podMetrics.getContainers().stream()
            .map(container -> container.getUsage().get("cpu"))
            .filter(Objects::nonNull)
            .mapToDouble(cpu -> cpu.getNumber().doubleValue())
            .sum();
    Long totalMemory =
        podMetrics.getContainers().stream()
            .map(container -> container.getUsage().get("memory"))
            .filter(Objects::nonNull)
            .mapToLong(memory -> memory.getNumber().longValue())
            .sum();
    entity.setCpuUsage(totalCpu);
    entity.setMemoryUsage(totalMemory);
    entity.setTimestamp(
        Objects.requireNonNull(podMetrics.getMetadata().getCreationTimestamp()).toLocalDateTime());
    entity.setCreatedAt(LocalDateTime.now());

    return entity;
  }
}
