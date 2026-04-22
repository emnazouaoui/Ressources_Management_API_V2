package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wevioo.example.resourcemanagementproject.Entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u
        LEFT JOIN u.role r
        LEFT JOIN u.department d
        WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.level) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<User> searchUsers(@Param("keyword") String keyword);
}
