package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
import wevioo.example.resourcemanagementproject.Validator.Annotation.ValidName;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTimeLineDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    @ValidName
    private String title;

    @Size(max = 1000)
    private String description;
    //private LocalDate eventDate; same CreatedDate

    @NotNull(message = "Project is required")
    private Long projectId;
    private String name;   // utile pour l'affichage

    @DecimalMin(value = "0.0", message = "Progress must be >= 0")
    @DecimalMax(value = "100.0", message = "Progress must be <= 100")
    private Double progressPercent;

    @Pattern(
            regexp = "^[0-9]+\\.[0-9]+(\\.[0-9]+)?$",
            message = "Version format must be like 1.0 or 1.0.0"
    )
    //✅ 1.0 ✅ 2.1 ✅ 1.0.5
    //❌ v1.0 (v interdit) ❌ 1 (pas de point) ❌ 1.0.0.1 (trop de niveaux)
    private String version;

    private Boolean deliveredToClient;

    @NotNull(message = "Project Timeline type is required")
    private ProjectTimeLineType type; //  ENUM

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
