package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_history")
public class UserHistory extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users", nullable = false)
    private User user;

    private String fieldChanged;
    private String oldValue;
    private String newValue;

//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User updatedBy;
//    private LocalDateTime updatedDate;



}

