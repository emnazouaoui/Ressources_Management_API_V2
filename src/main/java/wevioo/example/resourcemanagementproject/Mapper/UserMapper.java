package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Entity.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "role.id",           target = "roleId")
    @Mapping(source = "role.name",         target = "roleName")
    @Mapping(source = "department.id",     target = "departmentId")
    @Mapping(source = "department.name",   target = "departmentName")
    @Mapping(source = "manager.id",        target = "managerId")
    @Mapping(source = "manager.username",  target = "managerUsername")
    @Mapping(source = "technologies",      target = "technologyIds",   qualifiedByName = "techsToIds")
    @Mapping(source = "technologies",      target = "technologyNames", qualifiedByName = "techsToNames")
    UserDTO UserToUserDTO(User entity);

    @Mapping(target = "role",         ignore = true)
    @Mapping(target = "department",   ignore = true)
    @Mapping(target = "manager",      ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "userProjects", ignore = true)
    @Mapping(target = "password",     ignore = true)
    @Mapping(source = "phone",        target = "phone")  // ← زيد هاذي
    User UserDTOtoUserEntity(UserDTO dto);

    @Mapping(target = "role",         ignore = true)
    @Mapping(target = "department",   ignore = true)
    @Mapping(target = "manager",      ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "userProjects", ignore = true)
    @Mapping(target = "password",     ignore = true)

    @Mapping(source = "phone",        target = "phone")  // ← زيد هاذي
    void updateUserEntityFromUserDTO(UserDTO dto, @MappingTarget User user);

    @Named("techsToIds")
    default List<Long> techsToIds(List<Technology> technologies) {
        if (technologies == null) return List.of();
        return technologies.stream().map(t -> t.getId()).toList();
    }

    @Named("techsToNames")
    default List<String> techsToNames(List<Technology> technologies) {
        if (technologies == null) return List.of();
        return technologies.stream().map(Technology::getName).toList();
    }
}
