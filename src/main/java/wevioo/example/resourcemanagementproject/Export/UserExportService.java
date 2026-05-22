package wevioo.example.resourcemanagementproject.Export;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.Level;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserExportService {

    private final UserRepository userRepository;
    private final ExcelExportService excelExportService;

    public byte[] exportUsers(
            String username,
            String firstName,
            String lastName,
            String email,
            Boolean active,
            Level level,
            Long roleId,
            String roleName,
            Long departmentId,
            String departmentName,
            Long managerId,
            String managerUsername,
            String phone
    ) throws IOException {

        // ─── Fetch data avec filtres — sans pagination ───────────
        List<User> users = userRepository.searchUsersForExport(
                normalize(username),
                normalize(firstName),
                normalize(lastName),
                normalize(email),
                active,
                level,
                normalize(phone),
                roleId,
                normalize(roleName),
                departmentId,
                normalize(departmentName),
                managerId,
                normalize(managerUsername)
        );

        // ─── Headers ────────────────────────────────────────────
        List<String> headers = List.of(
                "ID", "Username", "First Name", "Last Name",
                "Email", "Phone", "Level", "Active",
                "Role", "Department", "Manager"
        );

        // ─── Rows ────────────────────────────────────────────────
        List<List<String>> rows = users.stream()
                .map(u -> List.of(
                        String.valueOf(u.getId()),
                        safe(u.getUsername()),
                        safe(u.getFirstName()),
                        safe(u.getLastName()),
                        safe(u.getEmail()),
                        safe(u.getPhone()),
                        u.getLevel() != null ? u.getLevel().name() : "",
                        u.getActive() != null ? (u.getActive() ? "Active" : "Inactive") : "",
                        u.getRole() != null ? u.getRole().getName() : "",
                        u.getDepartment() != null ? u.getDepartment().getName() : "",
                        u.getManager() != null ? u.getManager().getUsername() : ""
                ))
                .toList();

        return excelExportService.export("Users", headers, rows);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

}
