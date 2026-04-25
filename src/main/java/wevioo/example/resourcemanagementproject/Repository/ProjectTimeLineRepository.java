package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;

import java.util.List;

@Repository
public interface ProjectTimeLineRepository extends JpaRepository<ProjectTimeLine, Long> {

    // 🔥 search
    @Query("""
    SELECT t FROM ProjectTimeLine t
    LEFT JOIN t.project p
    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(t.version) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(t.type) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))

    """)
    List<ProjectTimeLine> search(@Param("keyword") String keyword);

    // 🔥 by project
    List<ProjectTimeLine> findByProjectId(Long projectId);

    void deleteByIdAndProjectId(Long id, Long projectId);

}
