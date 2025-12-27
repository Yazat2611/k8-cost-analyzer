package in.k8s.costanalyzer.service;

import in.k8s.costanalyzer.dto.PodDto;
import in.k8s.costanalyzer.entity.PodEntity;
import in.k8s.costanalyzer.mapper.PodMapper;
import in.k8s.costanalyzer.repository.PodRepositroy;
import io.kubernetes.client.Metrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PodService {

  private final CoreV1Api api;
  private final Metrics metrics;
  private final PodRepositroy podRepositroy;
  private final KafkaProducerService kafkaProducerService;

  public PodService(PodRepositroy podRepositroy,KafkaProducerService kafkaProducerService) throws IOException {
    ApiClient client = Config.defaultClient();
    Configuration.setDefaultApiClient(client);
    this.metrics = new Metrics(client);
    this.api = new CoreV1Api();
    this.podRepositroy = podRepositroy;
    this.kafkaProducerService = kafkaProducerService;
  }

  public List<PodDto> getAllPods() throws ApiException {
    V1PodList list =
        api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
    List<PodDto> pods = new ArrayList<>();

    for (V1Pod pod : list.getItems()) {
      PodDto dto = new PodDto();

      dto.setName(pod.getMetadata().getName());
      dto.setNamespace(pod.getMetadata().getNamespace());
      dto.setStatus(pod.getStatus().getPhase());
      dto.setNodeName(pod.getSpec().getNodeName());
      dto.setCreatedAt(pod.getMetadata().getCreationTimestamp().toString());

      Long restartCount = 0L;
      if (pod.getStatus().getContainerStatuses() != null) {
        for (V1ContainerStatus cs : pod.getStatus().getContainerStatuses()) {
          restartCount += cs.getRestartCount();
        }
      }
      dto.setRestartCount(restartCount);

      pods.add(dto);
    }

    return pods;
  }

  public V1PodList getPodsInNamespace(String namespace) throws ApiException {
    return api.listNamespacedPod(
        namespace, null, null, null, null, null, null, null, null, null, null);
  }

  public V1NamespaceList getAllNamespaces() throws ApiException {
    return api.listNamespace(null, null, null, null, null, null, null, null, null, null);
  }

  public PodMetricsList getPodMetrics(String namespace) throws ApiException {
    PodMetricsList list = metrics.getPodMetrics(namespace);

    for (PodMetrics podMetrics : list.getItems()) {
      PodEntity entity = PodMapper.convertToEntity(podMetrics);

      podRepositroy.save(entity);
      kafkaProducerService.publishMetric(entity);
    }

    return list;
  }

  public PodMetrics getSelectivePodMetrics(String namespace, String podName) throws ApiException {
    PodMetricsList list = metrics.getPodMetrics(namespace);

    return list.getItems().stream()
        .filter(pm -> pm.getMetadata().getName().equals(podName))
        .findFirst()
        .orElse(null);
  }

  public void collectMetricsForAllNamespaces() throws ApiException {

    V1NamespaceList namespaceList = getAllNamespaces();

    for(V1Namespace ns : namespaceList.getItems()) {
      String namespaceName = ns.getMetadata().getName();

      try {
        getPodMetrics(namespaceName);
      } catch (ApiException e) {
          log.error("Failed for namespace: {}", namespaceName);
      }
    }
  }
}
