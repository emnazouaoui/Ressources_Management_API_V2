package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Entity.Department;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {
    public static DepartmentDTO toDto(Department d) {
        if (d == null) return null;

        return DepartmentDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .createdDate(d.getCreatedDate())
                .updatedDate(d.getUpdatedDate())
                .build();
    }

    public static Department toEntity(DepartmentDTO dto) {
        if (dto == null) return null;

        Department d = new Department();
        d.setId(dto.getId());
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        d.setCreatedDate(dto.getCreatedDate());
        d.setUpdatedDate(dto.getUpdatedDate());
        return d;
    }

    public static List<DepartmentDTO> toDtoList(List<Department> list) {
        return list.stream()
                .map(DepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

}
