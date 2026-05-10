package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Technology;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    @Query("SELECT t FROM Technology t "
            + "WHERE (:name IS NULL OR t.name LIKE %:name%) ")
    Page<Technology> searchTechnologies(
            @Param("name")  String name,
            Pageable pageable
    );

}
