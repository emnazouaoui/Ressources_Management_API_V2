package wevioo.example.resourcemanagementproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImputationDTO {

    private Long id;
    private LocalDateTime date;
    private Double hours;
    private String comment;

    // Relations — on expose juste les IDs pour éviter les boucles infinies
    private Long taskId;
    private String title;   // utile pour l'affichage



    private Long userId;
    private String username;   // utile pour l'affichage


    private Long createdById;
    private LocalDateTime createdDate;

    private Long updatedById;
    private LocalDateTime updatedDate;
}
