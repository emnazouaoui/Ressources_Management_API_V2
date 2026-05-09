package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

//    @Query("""
//        SELECT t FROM Task t
//        LEFT JOIN t.assignedUser u
//        LEFT JOIN t.project p
//        WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(t.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(t.priority) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR CAST(t.startDate AS string) LIKE CONCAT('%', :keyword, '%')
//       OR CAST(t.endDate AS string) LIKE CONCAT('%', :keyword, '%')
//    """)
//    List<Task> searchTasks(@Param("keyword") String keyword);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.imputations WHERE t.id = :id")
    Optional<Task> findByIdWithImputations(Long id);

    @Query("SELECT t FROM Task t "
            + "LEFT JOIN t.project p "
            + "LEFT JOIN t.assignedUser u "
            + "WHERE ((:title) IS NULL OR t.title LIKE %:title%) "
            + "AND ((:description) IS NULL OR t.description LIKE %:description% ) "
            + "AND ((:status) IS NULL OR t.status = :status) "
            + "AND ((:priority) IS NULL OR t.priority = :priority) "
            + "AND ((:projectId) IS NULL OR p.id = :projectId) "
            + "AND ((:projectName) IS NULL OR p.name LIKE %:projectName%) "
            + "AND ((:assignedUserId) IS NULL OR u.id = :assignedUserId) "
            + "AND ((:assignedUserUsername) IS NULL OR u.username LIKE  %:assignedUserUsername% ) "
            + "AND (CAST(:startDate AS timestamp) IS NULL OR t.startDate >= :startDate) "
            + "AND (CAST(:endDate AS timestamp) IS NULL OR t.endDate <= :endDate) "
            + "AND (CAST(:estimatedHours AS double) IS NULL OR t.estimatedHours = :estimatedHours) "
            + "AND (CAST(:consumedHours AS double) IS NULL OR t.consumedHours = :consumedHours)")
    Page<Task> searchTasks(
            @Param("title")                String     title,
            @Param("description")          String     description,
            @Param("status") TaskStatus status,
            @Param("priority") Priority priority,
            @Param("projectId")            Long       projectId,
            @Param("projectName")          String     projectName,
            @Param("assignedUserId")       Long       assignedUserId,
            @Param("assignedUserUsername") String     assignedUserUsername,
            @Param("startDate")            LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("estimatedHours")       Double     estimatedHours,
            @Param("consumedHours")        Double     consumedHours,
            Pageable pageable
    );

//    OR CAST(t.estimatedHours AS TEXT) LIKE CONCAT('%', :keyword, '%')
//    OR CAST(t.consumedHours AS TEXT) LIKE CONCAT('%', :keyword, '%')
}
