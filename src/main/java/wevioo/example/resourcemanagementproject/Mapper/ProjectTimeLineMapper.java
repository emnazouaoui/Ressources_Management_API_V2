package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;


@Component
public class ProjectTimeLineMapper {

    public ProjectTimeLineDTO toDTO(ProjectTimeLine t) {
        return ProjectTimeLineDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .progressPercent(t.getProgressPercent())
                .version(t.getVersion())
                .deliveredToClient(t.getDeliveredToClient())
                .type(t.getType())
                .projectId(t.getProject() != null ? t.getProject().getId() : null)
                .build();
    }

    public void updateEntity(ProjectTimeLineDTO dto, ProjectTimeLine t) {

        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setProgressPercent(dto.getProgressPercent());
        t.setVersion(dto.getVersion());
        t.setDeliveredToClient(dto.getDeliveredToClient());
        t.setType(dto.getType());
    }

}
