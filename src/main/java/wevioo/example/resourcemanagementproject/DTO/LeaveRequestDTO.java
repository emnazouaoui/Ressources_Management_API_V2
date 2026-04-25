package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {

    private Long id;

    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;

    @NotNull(message = "Project Manager is required")
    private Long projectManagerId;

    @NotNull(message = "User is required")
    private Long userId;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
