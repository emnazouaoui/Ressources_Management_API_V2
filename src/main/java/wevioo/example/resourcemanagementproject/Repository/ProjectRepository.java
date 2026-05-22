package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p "
            + "LEFT JOIN p.projectManager pm "
            + "LEFT JOIN p.client c "
            + "WHERE (:name IS NULL OR p.name LIKE %:name%) "
            + "AND ((:description) IS NULL OR p.description LIKE %:description%) "
            + "AND ((:status) IS NULL OR p.status = :status) "
            + "AND ((:projectManagerId) IS NULL OR pm.id = :projectManagerId) "
            + "AND ((:projectManagerUsername) IS NULL OR pm.username LIKE %:projectManagerUsername%) "
            + "AND ((:clientId) IS NULL OR c.id = :clientId) "
            + "AND ((:clientName) IS NULL OR c.name LIKE %:clientName%) "
            + "AND (CAST(:startDate AS timestamp) IS NULL OR p.startDate = :startDate) "
            + "AND (CAST(:endDate AS timestamp) IS NULL OR p.endDate = :endDate) "
            + "AND (CAST(:progressPercent AS double)IS NULL OR p.progressPercent = :progressPercent)")
    Page<Project> searchProjects(
            @Param("name")                   String        name,
            @Param("description")            String        description,
            @Param("status")                 ProjectStatus status,
            @Param("projectManagerId")       Long          projectManagerId,
            @Param("projectManagerUsername") String        projectManagerUsername,
            @Param("clientId")               Long          clientId,
            @Param("clientName")             String        clientName,
            @Param("startDate")              LocalDateTime startDate,
            @Param("endDate")                LocalDateTime endDate,
            @Param("progressPercent")        Double        progressPercent,
            Pageable pageable
    );

    //  searchProjects for export
    @Query("SELECT p FROM Project p "
            + "LEFT JOIN p.projectManager pm "
            + "LEFT JOIN p.client c "
            + "WHERE ((:name) IS NULL OR p.name LIKE %:name%) "
            + "AND ((:description) IS NULL OR p.description LIKE %:description%) "
            + "AND ((:status) IS NULL OR p.status = :status) "
            + "AND ((:projectManagerId) IS NULL OR pm.id = :projectManagerId) "
            + "AND ((:projectManagerUsername) IS NULL OR pm.username LIKE %:projectManagerUsername%) "
            + "AND ((:clientId) IS NULL OR c.id = :clientId) "
            + "AND ((:clientName) IS NULL OR c.name LIKE %:clientName%) "
            + "AND (CAST(:startDate AS timestamp) IS NULL OR p.startDate = :startDate) "
            + "AND (CAST(:endDate AS timestamp) IS NULL OR p.endDate = :endDate) "
            + "AND (CAST(:progressPercent AS double) IS NULL OR p.progressPercent = :progressPercent)")
    List<Project> searchProjectsForExport(    // ← List مش Page
                                              @Param("name")                   String        name,
                                              @Param("description")            String        description,
                                              @Param("status")                 ProjectStatus status,
                                              @Param("projectManagerId")       Long          projectManagerId,
                                              @Param("projectManagerUsername") String        projectManagerUsername,
                                              @Param("clientId")               Long          clientId,
                                              @Param("clientName")             String        clientName,
                                              @Param("startDate")              LocalDateTime startDate,
                                              @Param("endDate")                LocalDateTime endDate,
                                              @Param("progressPercent")        Double        progressPercent
    );

}
