package wevioo.example.resourcemanagementproject.Service;

import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;

@Service
public class LeavePolicyService {

    public int maxDays(LeaveRequestType type) {
        return switch (type) {
            case SICK -> 5;
           // case UNPAID -> 5;
            case PATERNITY -> 2;
            case MATERNITY -> 60;
            case ANNUAL -> Integer.MAX_VALUE;
        };
    }
}
