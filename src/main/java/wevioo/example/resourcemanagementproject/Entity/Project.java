package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project")
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

    /** Team members assigned to this project */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjectsList= new ArrayList<>();;

    /** Technologies used in this project */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTechnology> ProjectsTechnologyList= new ArrayList<>();;

    /** Timeline events (milestones, deliveries, reviews…) */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTimeLine> ProjectsTimelineList= new ArrayList<>();;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

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

