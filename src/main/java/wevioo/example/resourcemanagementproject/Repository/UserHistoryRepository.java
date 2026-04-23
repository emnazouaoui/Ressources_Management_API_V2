package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {
}
