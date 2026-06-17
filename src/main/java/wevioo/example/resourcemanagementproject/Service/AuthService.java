package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LoginRequest;
import wevioo.example.resourcemanagementproject.DTO.LoginResponse;
import wevioo.example.resourcemanagementproject.DTO.RegisterRequest;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Exception.Custom.ConflictException;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Exception.Custom.UnauthorizedException;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;
import wevioo.example.resourcemanagementproject.DTO.ForgetPasswordRequest;
import wevioo.example.resourcemanagementproject.JWT.JwtService;
import wevioo.example.resourcemanagementproject.Validator.Impl.AuthValidator;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;
    private final AuthValidator authValidator;   // ← validation centralisée

    // ─── LOGIN ───────────────────────────────────────────────
    // Toute la logique qui était dans AuthController.login() est ici
    public LoginResponse login(LoginRequest request) {

        //  Authentifie via Spring Security (vérifie email + password)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        //  Récupère le user complet pour générer le token
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String token = jwtService.generateToken(user);

        return buildLoginResponse(user, token);
    }

    // ─── REGISTER ────────────────────────────────────────────
    public LoginResponse register(RegisterRequest request) {

        //  Validation (pattern password réutilisé)
        authValidator.validateRegister(request);

        //  Email unique
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));   // 🔥 hash
        user.setPhone(request.getPhone());
        user.setLevel(request.getLevel());
        user.setActive(true);
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());

        user.setRole(roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));

        user.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));

        if (request.getManagerId() != null) {
            user.setManager(userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        }

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return buildLoginResponse(saved, token);
    }

    // ─── LOGOUT ──────────────────────────────────────────────
    // Ajoute le token courant à la blacklist → devient invalide immédiatement
    public void logout(String token) {
        tokenBlacklist.add(token);
    }

    // ─── FORGET PASSWORD ─────────────────────────────────────
    public void forgetPassword(ForgetPasswordRequest request) {

        //  Validation (pattern password + confirmPassword match)
        authValidator.validateForgetPassword(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    // ─── Helper — construit LoginResponse ────────────────────
    private LoginResponse buildLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .expiresIn(jwtService.getExpiration())
                .build();
    }

}
