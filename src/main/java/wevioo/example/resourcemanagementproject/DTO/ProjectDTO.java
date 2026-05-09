package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    private Double progressPercent;

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
