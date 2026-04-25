package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDate startDate;
    private LocalDate endDate;

    @DecimalMin(value = "0.0") @DecimalMax(value = "20.0")
    private BigDecimal estimatedHours;

    @DecimalMin(value = "0.0") @DecimalMax(value = "20.0")
    private BigDecimal consumedHours;

    @NotNull(message = "Project is required")
    private Long projectId;

    @NotNull(message = "User assigned is required")
    private Long assignedUserId;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
