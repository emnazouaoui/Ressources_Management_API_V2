package wevioo.example.resourcemanagementproject.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
