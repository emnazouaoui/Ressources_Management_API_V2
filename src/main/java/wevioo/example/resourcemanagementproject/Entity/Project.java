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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Listener.ProjectEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project")
@EntityListeners(ProjectEntityListener.class)
public class Project{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private Double progressPercent= null; // or 0.0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectManager", nullable = false)
    private User projectManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", nullable = false)
    private Client client;

    /** All tasks within this project */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasksList= new ArrayList<>(); // without exception/ without verification of empty list

    /** Timeline events (milestones, deliveries, reviews…) */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTimeLine> ProjectsTimelineList= new ArrayList<>();;

    //  Remplace par
    @ManyToMany
    @JoinTable(
            name = "project_technology",
            joinColumns = @JoinColumn(name = "project"),
            inverseJoinColumns = @JoinColumn(name = "technology")
    )
    private List<Technology> technologies = new ArrayList<>();

    // UserProject reste OneToMany (attributs supplémentaires)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Transient
    private Project oldProject;

    @Transient
    private String oldName;

    @Transient
    private String oldDescription;

    @Transient
    private String oldStatus;

    @Transient
    private String oldStartDate;

    @Transient
    private String oldEndDate;

    @Transient
    private String oldProgress;

    @Transient
    private String oldManager;

    @Transient
    private String oldClient;

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
}

