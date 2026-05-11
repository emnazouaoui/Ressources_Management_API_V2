package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;
import wevioo.example.resourcemanagementproject.Validator.Annotation.ValidName;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    @ValidName
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @DecimalMin(value = "0.0", message = "Estimated hours must be >= 0")
    private Double estimatedHours;

    @DecimalMin(value = "0.0", message = "Consumed hours must be >= 0")
    private Double consumedHours;

    @NotNull(message = "Project is required")
    private Long projectId;
    private String projectName;

    @NotNull(message = "Assigned user is required")
    private Long assignedUserId;
    private String assignedUserUsername;

    private List<Long> imputationIds;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
