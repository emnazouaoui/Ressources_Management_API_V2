package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Validator.Annotation.ValidName;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @NotNull(message = "Status is required")
    private String status;

    @DecimalMin(value = "0.0", message = "Progress must be >= 0")
    @DecimalMax(value = "100.0", message = "Progress must be <= 100")
    private Double progressPercent;

    @NotNull(message = "Project manager is required")
    private Long projectManagerId;
    private String projectManagerUsername;

    @NotNull(message = "Client is required")
    private Long clientId;
    private String clientName;


    // relations (IDs)
    private List<Long> userIds;        // team members (UserProject)
    //private List<Long> technologyIds;// ProjectTechnology
    private List<ProjectTimeLineDTO> timelines; // TimeLines project
    private List<Long> taskIds;

    // ✅ Après — plus d'info sur les techs
    private List<Long> technologyIds;      // garde pour assign/remove
    private List<String> technologyNames;  // ajoute pour l'affichage

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
