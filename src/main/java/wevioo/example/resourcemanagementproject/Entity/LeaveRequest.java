package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Leave_request")
public class LeaveRequest extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private LeaveRequestType type;

    private LocalDate startDate;
    private LocalDate  endDate;
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveRequestStatus status;

    //private Date requestDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectManager", nullable = false)
    private User projectManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users", nullable = false)
    private User user;

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

