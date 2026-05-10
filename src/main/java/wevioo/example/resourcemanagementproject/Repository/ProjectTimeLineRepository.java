package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;

import java.util.List;

@Repository
public interface ProjectTimeLineRepository extends JpaRepository<ProjectTimeLine, Long> {

//    // search
//    @Query("""
//    SELECT t FROM ProjectTimeLine t
//    LEFT JOIN t.project p
//    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
//       OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
//       OR LOWER(t.version) LIKE LOWER(CONCAT('%', :keyword, '%'))
//       OR LOWER(t.type) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//
//    """)
//    List<ProjectTimeLine> search(@Param("keyword") String keyword);

    @Query("SELECT pt FROM ProjectTimeLine pt "
            + "JOIN pt.project p "
            + "WHERE (:title IS NULL OR pt.title LIKE %:title%) "
            + "AND ((:description) IS NULL OR pt.description LIKE %:description%) "
            + "AND ((:version) IS NULL OR pt.version LIKE %:version%) "
            + "AND ((:type) IS NULL OR pt.type = :type) "
            + "AND ((:deliveredToClient) IS NULL OR pt.deliveredToClient = :deliveredToClient) "
            + "AND ((:projectId) IS NULL OR p.id = :projectId) "
            + "AND ((:name) IS NULL OR p.name LIKE %:name%)"
            + "AND (CAST(:progressPercent AS double) IS NULL OR pt.progressPercent = :progressPercent) ")
    Page<ProjectTimeLine> searchProjectTimeLines(
            @Param("title")             String               title,
            @Param("description")       String               description,
            @Param("version")           String               version,
            @Param("type")              ProjectTimeLineType  type,
            @Param("deliveredToClient") Boolean              deliveredToClient,
            @Param("projectId")         Long                 projectId,
            @Param("name")       String               name,
            @Param("progressPercent")       Double               progressPercent,
            Pageable pageable
    );


    // 🔥 by project
    List<ProjectTimeLine> findByProjectId(Long projectId);

   // void deleteByIdAndProjectId(Long id, Long projectId);

}
