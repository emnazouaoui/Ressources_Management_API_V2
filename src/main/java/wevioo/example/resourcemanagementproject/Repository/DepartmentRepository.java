package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d "
            + "WHERE (:name IS NULL OR d.name LIKE %:name%) "
            + "AND ((:description) IS NULL OR d.description LIKE %:description%)")
    Page<Department> searchDepartments(
            @Param("name")        String name,
            @Param("description") String description,
            Pageable pageable
    );


}
