package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
