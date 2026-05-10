package wevioo.example.resourcemanagementproject.DTO;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ImputationDTO {

    private Long id;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotNull(message = "Hours is required")
    @DecimalMin(value = "0.1", message = "Hours must be at least 0.1")
    @DecimalMax(value = "24.0", message = "Hours cannot exceed 24")
    private Double hours;

    @Size(max = 500)
    private String comment;

    // Relations — on expose juste les IDs pour éviter les boucles infinies
    @NotNull(message = "Task is required")
    private Long taskId;
    private String title;   // utile pour l'affichage

    @NotNull(message = "User is required")
    private Long userId;
    private String username;   // utile pour l'affichage


    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
