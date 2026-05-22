package wevioo.example.resourcemanagementproject.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Entity.UserProject;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProjectTimeLineMapper.class})
public interface ProjectMapper {

    @Mapping(source = "projectManager.id",       target = "projectManagerId")
    @Mapping(source = "projectManager.username",  target = "projectManagerUsername")
    @Mapping(source = "client.id",               target = "clientId")
    @Mapping(source = "client.name",             target = "clientName")
    @Mapping(source = "technologies",            target = "technologyIds",   qualifiedByName = "techsToIds")
    @Mapping(source = "technologies",            target = "technologyNames", qualifiedByName = "techsToNames")
    @Mapping(source = "userProjects",            target = "userIds",         qualifiedByName = "userProjectsToIds")
    @Mapping(source = "tasksList",               target = "taskIds",         qualifiedByName = "tasksToIds")
    @Mapping(source = "projectsTimelineList", target = "timelines")
    ProjectDTO ProjectToProjectDTO(Project entity);

    @Mapping(target = "projectManager",       ignore = true)
    @Mapping(target = "client",               ignore = true)
    @Mapping(target = "technologies",         ignore = true)
    @Mapping(target = "userProjects",         ignore = true)
    @Mapping(target = "tasksList",            ignore = true)
    @Mapping(target = "projectsTimelineList", ignore = true)  // fil toEntity
    Project ProjectDTOtoProjectEntity(ProjectDTO dto);

    @Mapping(target = "projectManager",       ignore = true)
    @Mapping(target = "client",               ignore = true)
    @Mapping(target = "technologies",         ignore = true)
    @Mapping(target = "userProjects",         ignore = true)
    @Mapping(target = "tasksList",            ignore = true)
    @Mapping(target = "projectsTimelineList", ignore = true)  // fil updateEntity
    void updateProjectEntity(ProjectDTO dto, @MappingTarget Project project);

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

    @Named("userProjectsToIds")
    default List<Long> userProjectsToIds(List<UserProject> userProjects) {
        if (userProjects == null) return List.of();
        return userProjects.stream().map(up -> up.getUser().getId()).toList();
    }

    @Named("tasksToIds")
    default List<Long> tasksToIds(List<Task> tasks) {
        if (tasks == null) return List.of();
        return tasks.stream().map(t -> t.getId()).toList();
    }
}
