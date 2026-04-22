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
public class TechnologyDTO {

    private Long id;

    @NotBlank(message = "Technology name is required")
    @Size(max = 100)
    private String name;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
