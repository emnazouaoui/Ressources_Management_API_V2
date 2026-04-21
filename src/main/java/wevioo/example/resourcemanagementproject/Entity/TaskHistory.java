package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "task_history")
public class TaskHistory extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    private String fieldChanged;
    private String oldValue;
    private String newValue;

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;
}
