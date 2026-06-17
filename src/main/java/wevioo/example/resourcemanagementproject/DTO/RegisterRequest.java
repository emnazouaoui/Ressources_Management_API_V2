package wevioo.example.resourcemanagementproject.DTO;

import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.Level;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Level level;
    private Long roleId;
    private Long departmentId;
    private Long managerId;
}
