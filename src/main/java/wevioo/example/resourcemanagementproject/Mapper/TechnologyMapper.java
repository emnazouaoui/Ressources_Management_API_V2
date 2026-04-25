package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TechnologyMapper {

    public static TechnologyDTO toDto(Technology t) {
        if (t == null) return null;

        return TechnologyDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .build();
    }

    public static Technology toEntity(TechnologyDTO dto) {
        if (dto == null) return null;

        Technology t = new Technology();
        t.setId(dto.getId());
        t.setName(dto.getName());

        return t;
    }

    public static List<TechnologyDTO> toDtoList(List<Technology> list) {
        return list.stream()
                .map(TechnologyMapper::toDto)
                .collect(Collectors.toList());
    }
}
