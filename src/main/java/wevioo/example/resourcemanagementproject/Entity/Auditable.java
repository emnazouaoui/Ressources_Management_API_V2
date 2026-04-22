package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

//    @CreatedBy
//    @Column(updatable = false)
//    private String createdBy;
//
//    @LastModifiedBy
//    private String updatedBy;


 //ba3ed lezem tetbadel type mte3ha User: wa9ta na3mel spring security
//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//    @ManyToOne
//    @JoinColumn(name = "updated_by")
//    private User updatedBy;


    //    @ManyToOne
//    private User createdBy;
//
//    private LocalDateTime createdDate;
//
//    @ManyToOne
//    private User updatedBy;
//
//    private LocalDateTime updatedDate;

}
