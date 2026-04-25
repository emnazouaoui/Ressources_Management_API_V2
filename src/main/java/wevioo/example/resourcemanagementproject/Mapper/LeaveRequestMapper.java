package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;

@Component
public class LeaveRequestMapper {

    public LeaveRequestDTO toDTO(LeaveRequest lr) {
        return LeaveRequestDTO.builder()
                .id(lr.getId())
                .type(lr.getType() != null ? lr.getType().name() : null)
                .startDate(lr.getStartDate())
                .endDate(lr.getEndDate())
                .reason(lr.getReason())
                .status(lr.getStatus() != null ? lr.getStatus().name() : null)
                .projectManagerId(lr.getProjectManager() != null ? lr.getProjectManager().getId() : null)
                .userId(lr.getUser() != null ? lr.getUser().getId() : null)
                .build();
    }

    public void toEntity(LeaveRequestDTO dto, LeaveRequest lr) {

        lr.setStartDate(dto.getStartDate());
        lr.setEndDate(dto.getEndDate());
        lr.setReason(dto.getReason());

        if (dto.getType() != null) {
            lr.setType(LeaveRequestType.valueOf(dto.getType()));
        }

        if (dto.getStatus() != null) {
            lr.setStatus(LeaveRequestStatus.valueOf(dto.getStatus()));
        }
    }
}
