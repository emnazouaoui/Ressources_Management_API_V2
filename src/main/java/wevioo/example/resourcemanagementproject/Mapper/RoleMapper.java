package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Entity.Role;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    RoleDTO RoleToRoleDTO(Role entity);
    Role RoleDTOtoRoleEntity(RoleDTO dto);
}
