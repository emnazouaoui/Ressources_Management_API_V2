package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.UserTechnology;

import java.util.List;

@Repository
public interface UserTechnologyRepository extends JpaRepository<UserTechnology, Long> {

    boolean existsByUserIdAndTechnologyId(Long userId, Long techId);

    void deleteByUserIdAndTechnologyId(Long userId, Long techId);

    @Query("""
        SELECT ut FROM UserTechnology ut
        WHERE ut.user.id = :userId
    """)
    List<UserTechnology> findByUserId(Long userId);

}
