package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public static RoleDTO toDto(Role r) {
        if (r == null) return null;

        return RoleDTO.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .active(r.getActive())
                .build();
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Role r = new Role();
        r.setId(dto.getId());
        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setActive(dto.getActive());

        return r;
    }

    public static List<RoleDTO> toDtoList(List<Role> list) {
        return list.stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }
}
