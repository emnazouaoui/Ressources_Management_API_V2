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
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //pour Login and security, for CustomUserDetailsService
    Optional<User> findByEmail(String email);

    Page<User> findByManagerId(Long managerId, Pageable pageable);

    //  find users by technology name
    @Query("SELECT u FROM User u JOIN u.technologies t WHERE t.name = :name")
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
            + "AND ((:phone) IS NULL OR u.phone LIKE %:phone%) "
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
            @Param("phone")           String  phone,
            @Param("roleId")          Long roleId,
            @Param("roleName")        String roleName,
            @Param("departmentId")    Long departmentId,
            @Param("departmentName")  String departmentName,
            @Param("managerId")       Long managerId,
            @Param("managerUsername") String managerUsername,
            Pageable pageable
    );

    // searchUsers for export
    @Query("SELECT u FROM User u "
            + "LEFT JOIN u.role r "
            + "LEFT JOIN u.department d "
            + "LEFT JOIN u.manager m "
            + "WHERE ((:username) IS NULL OR u.username LIKE %:username%) "
            + "AND ((:firstName) IS NULL OR u.firstName LIKE %:firstName%) "
            + "AND ((:lastName) IS NULL OR u.lastName LIKE %:lastName%) "
            + "AND ((:email) IS NULL OR u.email LIKE %:email%) "
            + "AND ((:active) IS NULL OR u.active = :active) "
            + "AND ((:level) IS NULL OR u.level = :level) "
            + "AND ((:phone) IS NULL OR u.phone LIKE  %:phone%) "
            + "AND ((:roleId) IS NULL OR r.id = :roleId) "
            + "AND ((:roleName) IS NULL OR r.name LIKE %:roleName%) "
            + "AND ((:departmentId) IS NULL OR d.id = :departmentId) "
            + "AND ((:departmentName) IS NULL OR d.name LIKE %:departmentName%) "
            + "AND ((:managerId) IS NULL OR m.id = :managerId) "
            + "AND ((:managerUsername) IS NULL OR m.username LIKE %:managerUsername%)")
    List<User> searchUsersForExport(    // ← List مش Page
                                        @Param("username")        String  username,
                                        @Param("firstName")       String  firstName,
                                        @Param("lastName")        String  lastName,
                                        @Param("email")           String  email,
                                        @Param("active")          Boolean active,
                                        @Param("level")           Level   level,
                                        @Param("phone")           String  phone,
                                        @Param("roleId")          Long    roleId,
                                        @Param("roleName")        String  roleName,
                                        @Param("departmentId")    Long    departmentId,
                                        @Param("departmentName")  String  departmentName,
                                        @Param("managerId")       Long    managerId,
                                        @Param("managerUsername") String  managerUsername
    );




}
