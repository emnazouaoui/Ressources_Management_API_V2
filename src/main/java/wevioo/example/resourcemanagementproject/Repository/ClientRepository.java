package wevioo.example.resourcemanagementproject.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Enums.ClientType;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

@Query("SELECT c FROM Client c "
        + "WHERE (:name IS NULL OR c.name LIKE %:name%) "
        + "AND (:email IS NULL OR c.email LIKE %:email%) "
        + "AND (:company IS NULL OR c.company LIKE %:company%) "
        + "AND (:address IS NULL OR c.address LIKE %:address%) "
        + "AND (:phone IS NULL OR c.phone LIKE %:phone%) "
        + "AND ((:typeClient) IS NULL OR c.typeClient = :typeClient)")

    Page<Client> searchClients(
            @Param("name")       String     name,
            @Param("email")      String     email,
            @Param("company")    String     company,
            @Param("address")    String     address,
            @Param("phone")      String     phone,
            @Param("typeClient") ClientType typeClient,
            Pageable pageable
    );

}
