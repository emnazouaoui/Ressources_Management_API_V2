package wevioo.example.resourcemanagementproject.DTO;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImputationDTO {

    private Long id;
    private LocalDate date;
    private BigDecimal hours;
    private String comment;

    private Long taskId;
    private Long userId;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
