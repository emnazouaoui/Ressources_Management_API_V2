package wevioo.example.resourcemanagementproject.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LeaveBalanceDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveBalance;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Repository.LeaveBalanceRepository;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository repository;

    public LeaveBalanceDTO getBalanceByUserId(Long userId) {

        // Just for annual leave request
        LeaveBalance lb = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Annual leave balance not found"));

        LeaveBalanceDTO dto = new LeaveBalanceDTO();

        dto.setUserId(userId);
        dto.setAnnualTotal(lb.getAnnualTotal());
        dto.setAnnualUsed(lb.getAnnualUsed());

        // 🔥 calculation of remaining days
        dto.setRemainingDays(lb.getAnnualTotal() - lb.getAnnualUsed());

        return dto;
    }
}
