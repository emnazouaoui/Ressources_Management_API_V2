package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    @Transactional   // ← زيد هاذي — تخلي الـ session مفتوحة بش يقدر يجيب الـ role
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ─── 1. جيب الـ user من الـ DB ──────────────────────
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // ─── 2. رجع UserDetails لـ Spring Security ──────────
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // username = email
                user.getPassword(),  // password encodé
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + (user.getRole() != null ? user.getRole().getName() : "USER")
                ))
        );
    }

}
