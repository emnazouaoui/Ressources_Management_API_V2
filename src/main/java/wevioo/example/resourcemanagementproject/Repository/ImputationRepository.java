package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Imputation;

import java.util.List;

@Repository
public interface ImputationRepository extends JpaRepository<Imputation, Long> {


    @Query("""
        SELECT i FROM Imputation i
        LEFT JOIN i.user u
        LEFT JOIN i.task t
        WHERE LOWER(i.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR CAST(i.date AS string) LIKE CONCAT('%', :keyword, '%')
        OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Imputation> searchByKeyword(String keyword);
}
