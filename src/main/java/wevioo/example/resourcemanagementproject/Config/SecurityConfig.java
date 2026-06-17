package wevioo.example.resourcemanagementproject.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wevioo.example.resourcemanagementproject.Service.CustomUserDetailsService;
import wevioo.example.resourcemanagementproject.JWT.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // Activer @PreAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;  // ← inject كـ field

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // ─── Public uniquement ────────────────────────────
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ─── Tout le reste → juste être connecté ─────────
                        // Les permissions se gèrent dans les services
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//
//                        // ─── Public ──────────────────────────────────────
//                        .requestMatchers(
//                                "/auth/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/v3/api-docs/**").permitAll()
//
//                        // ─── Roles only ──────────────────────────────────
//                        .requestMatchers("/api/roles/**").hasAuthority("ROLE_Admin")
//
//                        // ─── Departments + Clients ────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/departments/**",
//                                "/api/clients/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//                        .requestMatchers("/api/departments/**",
//                                "/api/clients/**").hasAuthority("ROLE_Admin")
//
//                        // ─── Users ───────────────────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/users/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//                        .requestMatchers(HttpMethod.POST,
//                                "/api/users").hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//                        .requestMatchers(HttpMethod.PUT,
//                                "/api/users/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//                        .requestMatchers(HttpMethod.DELETE,
//                                "/api/users/**").hasAuthority("ROLE_Admin")
//
//                        // ─── Projects ────────────────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/projects/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//                        .requestMatchers("/api/projects/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//
//                        // ─── Tasks ───────────────────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/tasks/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//                        .requestMatchers("/api/tasks/**").hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//
//                        // ─── Leave Requests ──────────────────────────────
//                        .requestMatchers("/api/leave-requests/**")
//                        .hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//
//                        // ─── Imputations ─────────────────────────────────
//                        .requestMatchers("/api/imputations/**")
//                        .hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//
//                        // ─── Technologies ────────────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/technologies/**")
//                        .hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//                        .requestMatchers("/api/technologies/**").hasAuthority("ROLE_Admin")
//
//                        // ─── Timeline ────────────────────────────────────
//                        .requestMatchers(HttpMethod.GET,
//                                "/api/project-timelines/**")
//                        .hasAnyAuthority("ROLE_Admin", "ROLE_Manager", "ROLE_User")
//                        .requestMatchers("/api/project-timelines/**")
//                        .hasAnyAuthority("ROLE_Admin", "ROLE_Manager")
//
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        //provider.setUserDetailsService(customUserDetailsService);  // ← type exact
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //1. pom.xml          → spring-security + jjwt
    //2. application.properties → jwt.secret + jwt.expiration
    //3. JwtService       → génère + valide tokens
    //4. JwtAuthFilter    → intercepte chaque requête
    //5. UserDetailsService → charge user par email
    //6. AuthController   → POST /auth/login
    //7. SecurityConfig   → configure les routes publiques/privées
    //8. SwaggerConfig    → ajoute bouton Authorize dans Swagger
    //9. Frontend         → save token dans localStorage

//    Login Request (email + password)
//         ↓
//                 AuthController.login()
//                 ↓
//                 AuthenticationManager.authenticate()
//                 ↓
//                 CustomUserDetailsService.loadUserByUsername(email)  ← هنا
//         ↓
//                 UserRepository.findByEmail(email)  ← يجيب من DB
//         ↓
//    Spring Security يقارن الـ password
//         ↓
//                 JwtService.generateToken()  ← يولد token
//         ↓
//    LoginResponse → Client


}
