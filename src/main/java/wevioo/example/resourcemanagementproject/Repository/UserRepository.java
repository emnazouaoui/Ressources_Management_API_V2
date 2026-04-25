package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.User;

import java.util.List;

@Repository
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

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN u.usersTechnologyList ut
    JOIN ut.technology t
    WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    List<User> findUsersByTechnologyName(@Param("name") String name);

//    @Query("""
//        SELECT DISTINCT u FROM User u
//        JOIN u.usersTechnologyList ut
//        WHERE ut.technology.id = :techId
//    """)
//    List<User> findUsersByTechnology(@Param("techId") Long techId);

}
