package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wevioo.example.resourcemanagementproject.Entity.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d " +
            "WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Department> searchByKeyword(@Param("keyword") String keyword);
}
