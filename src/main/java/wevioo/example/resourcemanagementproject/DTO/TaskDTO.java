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
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Task name is required")
    private String title;

    private String description;

    private TaskStatus status;
    private Priority priority;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @DecimalMin(value = "0.0") @DecimalMax(value = "20.0")
    private Double estimatedHours;

    @DecimalMin(value = "0.0") @DecimalMax(value = "20.0")
    private Double consumedHours;

    @NotNull(message = "Project is required")
    private Long projectId;

    @NotNull(message = "User assigned is required")
    private Long assignedUserId;

    private List<Long> imputationIds;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
