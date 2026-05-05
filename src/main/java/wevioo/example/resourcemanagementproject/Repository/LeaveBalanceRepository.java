package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wevioo.example.resourcemanagementproject.Entity.LeaveBalance;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByUserId(Long userId);
}
