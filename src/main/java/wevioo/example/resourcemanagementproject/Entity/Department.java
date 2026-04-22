package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "department")
public class Department extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Department name e.g. "Engineering", "HR" */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Optional description of the department's responsibilities */
    @Column(length = 500)
    private String description;

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
