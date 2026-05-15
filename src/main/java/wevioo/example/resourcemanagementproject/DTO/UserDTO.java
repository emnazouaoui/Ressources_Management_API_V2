package wevioo.example.resourcemanagementproject.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.Level;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    //  password (WRITE ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    private Boolean active;

    private String phone;

    //private String photo;

    @NotNull(message = "Role is required")
    private Long roleId;
    private String roleName;

    @NotNull(message = "Level is required")
    private Level level;

    @NotNull(message = "Department is required")
    private Long departmentId;
    private String departmentName;

    private Long managerId;
    private String managerUsername;

    // 🔥 relation

    // ✅ Ajoute
    private List<Long> technologyIds;
    private List<String> technologyNames;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;

}
