package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;
import wevioo.example.resourcemanagementproject.Listener.TaskEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "task")
@EntityListeners(TaskEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double estimatedHours= null;
    private Double consumedHours= null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedUser", nullable = false)
    private User assignedUser;

    // relation
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Imputation> imputations = new ArrayList<>();

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Transient
    private Task oldTask;

    @Transient
    private String oldTitle;

    @Transient
    private String oldDescription;

    @Transient
    private String oldStatus;

    @Transient
    private String oldPriority;

    @Transient
    private String oldStartDate;

    @Transient
    private String oldEndDate;

    @Transient
    private String oldEstimatedHours;

    @Transient
    private String oldConsumedHours;

    @Transient
    private String oldProject;

    @Transient
    private String oldAssignedUser;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedDate = LocalDateTime.now();
    }

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//    private LocalDateTime createdDate;
//
//    @ManyToOne
//    @JoinColumn(name = "updated_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;

//    // ✅ Champs temporaires — لا يتسجلوا في الـ DB
//    @Transient
//    private String _oldTitle;
//    @Transient
//    private String _oldDescription;
//    @Transient
//    private String _oldStatus;
//    @Transient
//    private String _oldPriority;
//    @Transient
//    private String _oldStartDate;
//    @Transient
//    private String _oldEndDate;
//    @Transient
//    private String _oldAssignedUser;
//
//    // ✅ يتكالى تلقائياً من Hibernate قبل الـ update
//    @PreUpdate
//    public void captureOldValues() {
//        this._oldTitle       = this.title;
//        this._oldDescription = this.description;
//        this._oldStatus      = this.status != null ? this.status.name() : null;
//        this._oldPriority    = this.priority != null ? this.priority.name() : null;
//        this._oldStartDate   = this.startDate != null ? this.startDate.toString() : null;
//        this._oldEndDate     = this.endDate != null ? this.endDate.toString() : null;
//        this._oldAssignedUser = this.assignedUser != null ? String.valueOf(this.assignedUser.getId()) : null;
//    }


}

