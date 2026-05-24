package wevioo.example.resourcemanagementproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaAuditing
@SpringBootApplication
public class ResourceManagementProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceManagementProjectApplication.class, args);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("Admin123!");
        System.out.println(hashed);
        //email: ines.guzeni@example.com
        // password: Admin123!
        // fil SQL: UPDATE users
        //SET password = '$2a$10$ejbgxzMaJtTzpDEKNSnqwuf.ZNkNCMxPopPZ.M80sI1pVOCfLoz9O'
        //WHERE email = 'ines.guzeni@example.com';
    }

}
