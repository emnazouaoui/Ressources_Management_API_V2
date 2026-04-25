package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
        SELECT t FROM Task t
        LEFT JOIN t.assignedUser u
        LEFT JOIN t.project p
        WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.priority) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR CAST(t.startDate AS string) LIKE CONCAT('%', :keyword, '%')
       OR CAST(t.endDate AS string) LIKE CONCAT('%', :keyword, '%')
    """)
    List<Task> searchTasks(@Param("keyword") String keyword);


//    OR CAST(t.estimatedHours AS TEXT) LIKE CONCAT('%', :keyword, '%')
//    OR CAST(t.consumedHours AS TEXT) LIKE CONCAT('%', :keyword, '%')
}
