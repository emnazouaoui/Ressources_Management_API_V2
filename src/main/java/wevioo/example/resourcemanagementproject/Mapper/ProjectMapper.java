package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Entity.Project;

import java.util.List;

@Component
public class ProjectMapper {

    private final ProjectTimeLineMapper timelineMapper;

    public ProjectMapper(ProjectTimeLineMapper timelineMapper) {
        this.timelineMapper = timelineMapper;
    }

    public ProjectDTO toDTO(Project p) {
        return ProjectDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .status(p.getStatus() != null ? p.getStatus().name() : null)
                .progressPercent(p.getProgressPercent())
                .projectManagerId(p.getProjectManager() != null ? p.getProjectManager().getId() : null)
                .clientId(p.getClient() != null ? p.getClient().getId() : null)

                // 🔥 USERS
                .userIds(
                        p.getUserProjectsList() != null ?
                                p.getUserProjectsList().stream()
                                        .map(up -> up.getUser().getId())
                                        .toList()
                                : null
                )

                // 🔥 TECHNOLOGIES
                .technologyIds(
                        p.getProjectsTechnologyList() != null ?
                                p.getProjectsTechnologyList().stream()
                                        .map(pt -> pt.getTechnology().getId())
                                        .toList()
                                : null
                )
                // 🔥 TIMELINES
                .timelines(
                        p.getProjectsTimelineList() != null ?
                                p.getProjectsTimelineList().stream()
                                        .map(timelineMapper::toDTO)
                                        .toList()
                                : List.of()
                )


                .build();
    }

    public void updateEntity(ProjectDTO dto, Project p) {
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        p.setProgressPercent(dto.getProgressPercent());

        // 🔥 status (string → enum)
        if (dto.getStatus() != null) {
            p.setStatus(Enum.valueOf(
                    wevioo.example.resourcemanagementproject.Enums.ProjectStatus.class,
                    dto.getStatus()
            ));
        }
    }
}
