package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Imputation;

import java.time.LocalDateTime;

@Repository
public interface ImputationRepository extends JpaRepository<Imputation, Long> {

    @Query("SELECT i FROM Imputation i "
            + "JOIN i.task t "
            + "JOIN i.user u "
            + "WHERE (:comment IS NULL OR i.comment LIKE %:comment%) "
            + "AND ((:title) IS NULL OR t.title LIKE %:title%) "
            + "AND ((:username) IS NULL OR u.username LIKE %:username%) "
            + "AND ((:taskId) IS NULL OR i.task.id = :taskId) "
            + "AND ((:userId) IS NULL OR i.user.id = :userId) "
            + "AND (CAST(:date AS timestamp) IS NULL OR i.date = :date) "
            + "AND (CAST(:hours AS double) IS NULL OR i.hours = :hours) ")
    Page<Imputation> searchImputations(
            @Param("comment")   String        comment,
            @Param("title")     String        title,
            @Param("username")  String        username,
            @Param("taskId")    Long          taskId,
            @Param("userId")    Long          userId,
            @Param("date")      LocalDateTime date,
            @Param("hours")     Double        hours,
            Pageable pageable
    );



}
