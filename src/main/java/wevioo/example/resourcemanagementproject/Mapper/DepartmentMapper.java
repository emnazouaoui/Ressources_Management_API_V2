package wevioo.example.resourcemanagementproject.Mapper;

import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Entity.Department;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentMapper {
    public static DepartmentDTO toDto(Department d) {
        if (d == null) return null;

        return DepartmentDTO.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .build();
    }

    public static Department toEntity(DepartmentDTO dto) {
        if (dto == null) return null;

        Department d = new Department();
        d.setId(dto.getId());
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        return d;
    }

    public static List<DepartmentDTO> toDtoList(List<Department> list) {
        return list.stream()
                .map(DepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

}
