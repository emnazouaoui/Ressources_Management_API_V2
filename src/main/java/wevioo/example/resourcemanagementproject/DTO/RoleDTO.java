package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RoleDTO {

    private Long id;

    @NotBlank(message = "Role name is required")
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    private Boolean active;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
