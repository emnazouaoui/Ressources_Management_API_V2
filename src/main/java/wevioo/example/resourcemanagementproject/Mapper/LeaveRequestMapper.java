package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LeaveRequestMapper {

    @Mapping(source = "user.id",                 target = "userId")
    @Mapping(source = "user.username",           target = "username")
    @Mapping(source = "projectManager.id",       target = "projectManagerId")
    @Mapping(source = "projectManager.username", target = "projectManagerName")
    LeaveRequestDTO toDTO(LeaveRequest entity);

    @Mapping(target = "user",           ignore = true)
    @Mapping(target = "projectManager", ignore = true)
    LeaveRequest toEntity(LeaveRequestDTO dto);

    // pour update
    @Mapping(target = "user",           ignore = true)
    @Mapping(target = "projectManager", ignore = true)
    void toEntity(LeaveRequestDTO dto, @MappingTarget LeaveRequest entity);
}