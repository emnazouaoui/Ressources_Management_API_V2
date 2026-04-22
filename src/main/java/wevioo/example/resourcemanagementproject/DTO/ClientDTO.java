package wevioo.example.resourcemanagementproject.DTO;

import lombok.Getter;
import lombok.Setter;
import wevioo.example.resourcemanagementproject.Enums.ClientType;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClientDTO {
    private Long id;

    private String name;
    private String address;
    private String company;
    private String email;
    private String phone;

    private ClientType typeClient;

    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;

}
