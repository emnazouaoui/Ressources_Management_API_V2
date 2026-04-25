package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "project_Timeline")
public class ProjectTimeLine extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    private String title;
    private String description;
   // private LocalDate eventDate;
    private BigDecimal progressPercent;
    private String version;
    private Boolean deliveredToClient= false;

    @Enumerated(EnumType.STRING)
    private ProjectTimeLineType type;

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

