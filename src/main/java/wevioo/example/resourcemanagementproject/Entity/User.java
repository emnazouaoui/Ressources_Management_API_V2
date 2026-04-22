package wevioo.example.resourcemanagementproject.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.Level;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager", nullable = true) // nullable = false normalement mais j'ai changé pour la 1ere creation
    private User manager;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTechnology> usersTechnologyList;

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

