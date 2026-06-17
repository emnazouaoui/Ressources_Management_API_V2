package wevioo.example.resourcemanagementproject.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Exception.Custom.UnauthorizedException;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    // ─── Retourne le user connecté ───────────────────────
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // check roles
    public void requireAdmin() {
        if (!isAdmin()) {
            throw new UnauthorizedException("Access denied: Admin role required");
        }
    }

    public void requireAdminOrManager() {
        if (!isAdmin() && !isManager()) {
            throw new UnauthorizedException("Access denied: Admin or Manager role required");
        }
    }

    public void requireAnyRole() {
        // juste être connecté — rien à faire
    }

    // ─── Checks ──────────────────────────────────────────
    public boolean isAdmin() {
        return hasRole("Admin");
    }

    public boolean isManager() {
        return hasRole("Manager");
    }

    public boolean isUser() {
        return hasRole("User");
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
