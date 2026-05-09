package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r "
            + "WHERE (:name IS NULL OR r.name LIKE %:name%) "
            + "AND ((:description) IS NULL OR r.description LIKE %:description%)")
    Page<Role> searchRoles(
            @Param("name")        String name,
            @Param("description") String description,
            Pageable pageable
    );

}
