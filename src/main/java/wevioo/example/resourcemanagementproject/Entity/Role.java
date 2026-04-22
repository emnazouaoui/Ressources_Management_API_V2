package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Unique role name e.g. ROLE_ADMIN */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /** Human-readable description of what this role can do */
    @Column(length = 255)
    private String description;

    /** Whether this role is currently active in the system */
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

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

