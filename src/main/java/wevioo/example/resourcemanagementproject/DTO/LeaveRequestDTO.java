package wevioo.example.resourcemanagementproject.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDTO {

    private Long id;

    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reason;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    // Relations — on expose juste les IDs pour éviter les boucles infinies
    @NotNull(message = "Project Manager is required")
    private Long projectManagerId;
    private String projectManagerName;   // utile pour l'affichage


    @NotNull(message = "User is required")
    private Long userId;
    private String username;   // utile pour l'affichage


    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
