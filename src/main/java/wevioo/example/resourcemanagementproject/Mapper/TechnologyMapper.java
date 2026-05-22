package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechnologyMapper {

    TechnologyDTO TechnologyToTechnologyDTO(Technology entity);
    Technology TechnologyDTOtoTechnologyEntity(TechnologyDTO dto);
}
