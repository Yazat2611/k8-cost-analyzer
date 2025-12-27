package in.k8s.costanalyzer.cron;

import in.k8s.costanalyzer.service.PodService;
import io.kubernetes.client.openapi.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class SchedulerService {
    private final PodService podService;

    @Scheduled(fixedDelay = 30000)
    public void collectMetrics() throws ApiException {
        log.info("Started scheduled metrics collection.......");
        podService.collectMetricsForAllNamespaces();
        log.info("Scheduled metrics collection is done");
    }
}
