package wevioo.example.resourcemanagementproject.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LeaveBalanceDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveBalance;
import wevioo.example.resourcemanagementproject.Repository.LeaveBalanceRepository;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository repository;

    public LeaveBalanceDTO getBalanceByUserId(Long userId) {

        LeaveBalance lb = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        LeaveBalanceDTO dto = new LeaveBalanceDTO();

        dto.setUserId(userId);
        dto.setAnnualTotal(lb.getAnnualTotal());
        dto.setAnnualUsed(lb.getAnnualUsed());

        // 🔥 calculation of remaining days
        dto.setRemainingDays(lb.getAnnualTotal() - lb.getAnnualUsed());

        return dto;
    }
}
