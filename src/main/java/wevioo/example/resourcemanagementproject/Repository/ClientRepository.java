package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wevioo.example.resourcemanagementproject.Entity.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("""
        SELECT c FROM Client c
        WHERE 
            LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.typeClient) LIKE LOWER(CONCAT('%', :keyword, '%'))
            
    """)
    List<Client> searchClients(@Param("keyword") String keyword);
}
