package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @ManyToOne
    private User createdBy;

    private LocalDateTime createdDate;

    @ManyToOne
    private User updatedBy;

    private LocalDateTime updatedDate;
}
