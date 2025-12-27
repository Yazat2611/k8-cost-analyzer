package in.k8s.costanalyzer.controller;

import in.k8s.costanalyzer.dto.PodDto;
import in.k8s.costanalyzer.service.PodService;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.custom.PodMetricsList;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pod")
@AllArgsConstructor
public class PodController {

  private final PodService podService;

  @GetMapping("/getPodDetails")
  public List<PodDto> getAllPods() throws ApiException {
    return podService.getAllPods();
  }

  @GetMapping("/{namespace}/getPodsInNamespace")
  public V1PodList getPodsInNamespace(@PathVariable("namespace") String namespace)
      throws ApiException {
    return podService.getPodsInNamespace(namespace);
  }

  @GetMapping("/getNamespace")
  public V1NamespaceList getNamespace() throws ApiException {
    return podService.getAllNamespaces();
  }

  @GetMapping("/{namespace}/getPodMetrics")
  public PodMetricsList getPodMetrics(@PathVariable("namespace") String namespace)
      throws ApiException {
    return podService.getPodMetrics(namespace);
  }

  @GetMapping("/{namespace}/{podName}/getSelectivePodMetrics")
  public PodMetrics getSelectivePodMetrics(
      @PathVariable("namespace") String namespace, @PathVariable("podName") String podName)
      throws ApiException {
    return podService.getSelectivePodMetrics(namespace, podName);
  }
}
