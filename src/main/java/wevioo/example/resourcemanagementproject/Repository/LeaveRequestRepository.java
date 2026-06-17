package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;

import java.time.LocalDateTime;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    Page<LeaveRequest> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.user.id = :userId OR lr.projectManager.id = :managerId")
    Page<LeaveRequest> findByUserIdOrProjectManagerId(
            @Param("userId") Long userId,
            @Param("managerId") Long managerId,
            Pageable pageable);

    @Query("SELECT lr FROM LeaveRequest lr "
            + "JOIN lr.user u "
            + "JOIN lr.projectManager pm "
            + "WHERE (:reason IS NULL OR lr.reason LIKE %:reason%) "
            + "AND ((:type) IS NULL OR lr.type = :type) "
            + "AND ((:status) IS NULL OR lr.status = :status) "
            + "AND ((:userId) IS NULL OR lr.user.id = :userId) "
            + "AND ((:projectManagerId) IS NULL OR lr.projectManager.id = :projectManagerId) "
            + "AND ((:username) IS NULL OR u.username LIKE %:username%) "
            //+ "AND ((:projectManagerName) IS NULL OR u.projectManagerName LIKE %:projectManagerName%) "
            + "AND (CAST(:startDate AS timestamp) IS NULL OR lr.startDate = :startDate) "
            + "AND (CAST(:endDate AS timestamp) IS NULL OR lr.endDate = :endDate)")
    Page<LeaveRequest> searchLeaveRequests(
            @Param("reason")           String             reason,
            @Param("type") LeaveRequestType type,
            @Param("status") LeaveRequestStatus status,
            @Param("userId")           Long               userId,
            @Param("projectManagerId") Long               projectManagerId,
            @Param("username")    String             username,
            @Param("startDate")        LocalDateTime      startDate,
            @Param("endDate")          LocalDateTime      endDate,
            Pageable pageable
    );

}
