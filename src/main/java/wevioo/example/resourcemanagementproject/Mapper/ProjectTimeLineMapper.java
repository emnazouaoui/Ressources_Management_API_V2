package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectTimeLineMapper {

    @Mapping(source = "project.id",   target = "projectId")
    @Mapping(source = "project.name", target = "name")
    ProjectTimeLineDTO toDTO(ProjectTimeLine entity);

    @Mapping(target = "project", ignore = true)
    ProjectTimeLine toEntity(ProjectTimeLineDTO dto);

    // pour update
    @Mapping(target = "project", ignore = true)
    void updateEntity(ProjectTimeLineDTO dto, @MappingTarget ProjectTimeLine entity);

//    @Mapping(target = "project", ignore = true)
//    void update(ProjectTimeLineDTO dto, @MappingTarget ProjectTimeLine entity);
}