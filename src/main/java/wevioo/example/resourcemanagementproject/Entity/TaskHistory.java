package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.*;
import wevioo.example.resourcemanagementproject.Enums.TaskField;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_history")
public class TaskHistory extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    private TaskField fieldChanged;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;
}
