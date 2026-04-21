package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "project_history")
public class ProjectHistory extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    private String fieldChanged;
    private String oldValue;
    private String newValue;

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;
}
