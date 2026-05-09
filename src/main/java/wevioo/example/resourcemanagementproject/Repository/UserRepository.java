package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.Level;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("""
//        SELECT u FROM User u
//        LEFT JOIN u.role r
//        LEFT JOIN u.department d
//        WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(u.level) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//    """)
//    List<User> searchUsers(@Param("keyword") String keyword);

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN u.usersTechnologyList ut
    JOIN ut.technology t
    WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))
""")
    List<User> findUsersByTechnologyName(@Param("name") String name);


    @Query("SELECT u FROM User u "
            + "LEFT JOIN u.role r "
            + "LEFT JOIN u.department d "
            + "LEFT JOIN u.manager m "
            + "WHERE ((:username) IS NULL OR u.username LIKE %:username% ) "
            + "AND ((:firstName) IS NULL OR u.firstName LIKE %:firstName% ) "
            + "AND ((:lastName) IS NULL OR u.lastName LIKE %:lastName% ) "
            + "AND ((:email) IS NULL OR u.email LIKE %:email%) "
            + "AND ((:active) IS NULL OR u.active = :active) "
            + "AND ((:level) IS NULL OR u.level = :level) "
            + "AND ((:roleId) IS NULL OR r.id = :roleId) "
            + "AND ((:roleName) IS NULL OR r.name LIKE %:roleName% ) "
            + "AND ((:departmentId) IS NULL OR d.id = :departmentId) "
            + "AND ((:departmentName) IS NULL OR d.name LIKE %:departmentName%) "
            + "AND ((:managerId) IS NULL OR m.id = :managerId) "
            + "AND ((:managerUsername) IS NULL OR m.username LIKE %:managerUsername% )")
    Page<User> searchUsers(
            @Param("username")        String username,
            @Param("firstName")       String firstName,
            @Param("lastName")        String lastName,
            @Param("email")           String email,
            @Param("active")          Boolean active,
            @Param("level")           Level level,
            @Param("roleId")          Long roleId,
            @Param("roleName")        String roleName,
            @Param("departmentId")    Long departmentId,
            @Param("departmentName")  String departmentName,
            @Param("managerId")       Long managerId,
            @Param("managerUsername") String managerUsername,
            Pageable pageable
    );




}
