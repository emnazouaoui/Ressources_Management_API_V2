package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wevioo.example.resourcemanagementproject.DTO.LeaveBalanceDTO;
import wevioo.example.resourcemanagementproject.Service.LeaveBalanceService;

@RestController
@RequestMapping("/api/leave-balance")
@RequiredArgsConstructor
@Tag(name = "Leave balance API", description = "Get Leave balance requests for users")
public class LeaveBalanceController {

    private final LeaveBalanceService service;

    // 🔥 GET remaining balance for user
    @Operation(summary = "Get remaining balance for annual leave request for user")
    @GetMapping("/{userId}")
    public LeaveBalanceDTO getBalance(@PathVariable Long userId) {
        return service.getBalanceByUserId(userId);
    }
}
