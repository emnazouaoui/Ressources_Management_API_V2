package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.ProjectHistory;

@Repository
public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory, Long> {
}
