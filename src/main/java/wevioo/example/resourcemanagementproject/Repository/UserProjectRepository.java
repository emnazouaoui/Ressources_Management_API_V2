package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wevioo.example.resourcemanagementproject.Entity.UserProject;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    void deleteByProjectIdAndUserId(Long projectId, Long userId);
}
