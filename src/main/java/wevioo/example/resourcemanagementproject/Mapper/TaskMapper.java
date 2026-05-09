package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Entity.Task;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {

    @Mapping(source = "project.id",            target = "projectId")
    @Mapping(source = "project.name",          target = "projectName")
    @Mapping(source = "assignedUser.id",       target = "assignedUserId")
    @Mapping(source = "assignedUser.username", target = "assignedUserUsername")
    TaskDTO toDTO(Task entity);

    @Mapping(target = "project",      ignore = true)
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "imputations",  ignore = true)
    Task toEntity(TaskDTO dto);
}
