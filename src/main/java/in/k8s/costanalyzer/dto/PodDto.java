package in.k8s.costanalyzer.dto;

import lombok.Data;

@Data
public class PodDto {
  private String name;
  private String namespace;
  private String status;
  private String nodeName;
  private String createdAt;
  private Long restartCount;
}
