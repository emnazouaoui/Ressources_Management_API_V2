package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTimeLineDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    //private LocalDate eventDate; same CreatedDate

    @NotNull(message = "Project is required")
    private Long projectId;

    private BigDecimal progressPercent;
    private String version;
    private Boolean deliveredToClient;

    private ProjectTimeLineType type; // ✅ ENUM

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
