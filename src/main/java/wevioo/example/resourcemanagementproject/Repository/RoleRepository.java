package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wevioo.example.resourcemanagementproject.Entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔎 Search by keyword (name + description)
    @Query("SELECT r FROM Role r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Role> searchByKeyword(@Param("keyword") String keyword);

}
