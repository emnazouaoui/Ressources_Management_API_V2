package wevioo.example.resourcemanagementproject.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import wevioo.example.resourcemanagementproject.Enums.ClientType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String address;

    @Size(max = 255)
    private String company;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20)
    private String phone;

    @NotNull(message = "Client type is required")
    private ClientType typeClient;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;



}
