package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import wevioo.example.resourcemanagementproject.DTO.ForgetPasswordRequest;
import wevioo.example.resourcemanagementproject.DTO.RegisterRequest;
import wevioo.example.resourcemanagementproject.DTO.LoginRequest;
import wevioo.example.resourcemanagementproject.DTO.LoginResponse;
import wevioo.example.resourcemanagementproject.Service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;   // ← seule dépendance !


    // ─── LOGIN ───────────────────────────────────────────────
    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // ─── REGISTER ────────────────────────────────────────────
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // ─── LOGOUT ──────────────────────────────────────────────
    @Operation(summary = "Logout — invalidate current token")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        //  Extrait le token du header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    // ─── FORGET PASSWORD ─────────────────────────────────────
    @Operation(summary = "Reset password by email")
    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordRequest request) {
        authService.forgetPassword(request);
        return ResponseEntity.ok("Password updated successfully");
    }

}
