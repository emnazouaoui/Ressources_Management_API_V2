package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImputationMapper {

    @Mapping(source = "task.id",       target = "taskId")
    @Mapping(source = "task.title",    target = "title")
    @Mapping(source = "user.id",       target = "userId")
    @Mapping(source = "user.username", target = "username")
    ImputationDTO toDTO(Imputation entity);

    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    Imputation toEntity(ImputationDTO dto);

    // ← default method pour 3 paramètres
    default Imputation toEntity(ImputationDTO dto, Task task, User user) {
        Imputation entity = toEntity(dto);
        entity.setTask(task);
        entity.setUser(user);
        return entity;
    }

    // ← default method pour update
    default void updateEntity(Imputation entity, ImputationDTO dto, Task task, User user) {
        entity.setDate(dto.getDate());
        entity.setHours(dto.getHours());
        entity.setComment(dto.getComment());
        entity.setTask(task);
        entity.setUser(user);
    }
}
