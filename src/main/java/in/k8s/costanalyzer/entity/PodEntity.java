package in.k8s.costanalyzer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pod_data")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PodEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "pod_name")
  private String podName;

  private String namespace;

  @Column(name = "cpu_usage")
  private Double cpuUsage;

  @Column(name = "memory_usage")
  private Long memoryUsage;

  @Column(name = "cpu_requested")
  private Double cpuRequested;

  @Column(name = "memory_requested")
  private Long memoryRequested;

  @Column(name = "timestamp", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime timestamp;

  @Column(name = "created_at", nullable = false, updatable = false)
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
}
