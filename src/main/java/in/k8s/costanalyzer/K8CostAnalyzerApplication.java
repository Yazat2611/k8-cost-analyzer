package in.k8s.costanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class K8CostAnalyzerApplication {

  public static void main(String[] args) {
    SpringApplication.run(K8CostAnalyzerApplication.class, args);
  }
}
