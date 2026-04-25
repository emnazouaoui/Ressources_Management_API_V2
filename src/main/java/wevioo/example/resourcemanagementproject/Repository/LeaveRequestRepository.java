package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("""
        SELECT l FROM LeaveRequest l
        LEFT JOIN l.user u
        LEFT JOIN l.projectManager pm
        WHERE
            LOWER(l.reason) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(CAST(l.type AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(CAST(l.status AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR CAST(l.startDate AS string) LIKE CONCAT('%', :keyword, '%')
            OR CAST(l.endDate AS string) LIKE CONCAT('%', :keyword, '%')
            OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(pm.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<LeaveRequest> search(@Param("keyword") String keyword);
}
