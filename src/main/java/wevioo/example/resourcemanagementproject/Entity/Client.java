package wevioo.example.resourcemanagementproject.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wevioo.example.resourcemanagementproject.Enums.ClientType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String company;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    /** INTERNAL = company department, EXTERNAL = outside client */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClientType typeClient;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JoinColumn(name = "createdDate")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JoinColumn(name = "updatedDate")
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

