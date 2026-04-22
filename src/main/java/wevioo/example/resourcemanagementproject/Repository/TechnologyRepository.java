package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wevioo.example.resourcemanagementproject.Entity.Technology;

import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    // 🔎 Search by keyword (JPQL)
    @Query("SELECT t FROM Technology t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Technology> searchByKeyword(@Param("keyword") String keyword);

}
