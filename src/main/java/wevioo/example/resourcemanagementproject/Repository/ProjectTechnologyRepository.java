package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.ProjectTechnology;

@Repository
public interface ProjectTechnologyRepository extends JpaRepository<ProjectTechnology, Long> {

    boolean existsByProjectIdAndTechnologyId(Long projectId, Long techId);

    void deleteByProjectIdAndTechnologyId(Long projectId, Long techId);
}
