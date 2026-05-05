package wevioo.example.resourcemanagementproject.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDTO {

    private Long userId;
    private int annualTotal;
    private int annualUsed;
    private int remainingDays;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
