package in.k8s.costanalyzer.repository;

import in.k8s.costanalyzer.entity.PodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PodRepositroy extends JpaRepository<PodEntity, Long> {}
