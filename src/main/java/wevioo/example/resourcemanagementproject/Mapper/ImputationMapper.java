package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;

@Component
public class ImputationMapper {

    public static ImputationDTO toDTO(Imputation entity) {
        if (entity == null) return null;

        return ImputationDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .hours(entity.getHours())
                .comment(entity.getComment())
                .taskId(entity.getTask() != null ? entity.getTask().getId() : null)
                .title(entity.getTask() != null ? entity.getTask().getTitle() : null)   // ← nouveau
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null) // ← nouveau
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }

    public static Imputation toEntity(ImputationDTO dto, Task task, User user) {
        if (dto == null) return null;

        Imputation entity = new Imputation();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setHours(dto.getHours());
        entity.setComment(dto.getComment());
        entity.setTask(task);
        entity.setUser(user);
        entity.setUpdatedDate(dto.getUpdatedDate());
        entity.setCreatedDate(dto.getCreatedDate());



        return entity;
    }

    public static void updateEntity(Imputation entity, ImputationDTO dto, Task task, User user) {
        entity.setDate(dto.getDate());
        entity.setHours(dto.getHours());
        entity.setComment(dto.getComment());
        entity.setTask(task);
        entity.setUser(user);
        entity.setUpdatedDate(dto.getUpdatedDate());
    }
}
