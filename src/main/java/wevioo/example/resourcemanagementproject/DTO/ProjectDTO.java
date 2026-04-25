package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    private BigDecimal progressPercent;

    private Long projectManagerId;

    @NotNull(message = "Client is required")
    private Long clientId;

    // relations (IDs)
    private List<Long> userIds;        // team members (UserProject)
    private List<Long> technologyIds;// ProjectTechnology
    private List<ProjectTimeLineDTO> timelines; // TimeLines project

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
