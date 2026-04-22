package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "technology")
public class Technology extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Technology name — unique, e.g. "Spring Boot", "React", "Docker" */
    @Column(nullable = false, unique = true, length = 155)
    private String name;

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//    private LocalDateTime createdDate;
//
//    @ManyToOne
//    @JoinColumn(name = "updated_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;

}
