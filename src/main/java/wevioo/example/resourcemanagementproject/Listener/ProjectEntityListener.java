package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PreUpdate;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectHistory;
import wevioo.example.resourcemanagementproject.Enums.ProjectField;
import wevioo.example.resourcemanagementproject.Repository.ProjectHistoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProjectEntityListener {


    @PostLoad
    public void postLoad(Project project) {

        project.setOldName(project.getName());
        project.setOldDescription(project.getDescription());

        project.setOldStatus(
                project.getStatus() != null
                        ? project.getStatus().name()
                        : null
        );
    }

    @PreUpdate
    public void preUpdate(Project project) {

        ProjectHistoryRepository repository =
                SpringContext.getBean(ProjectHistoryRepository.class);

        List<ProjectHistory> histories = new ArrayList<>();

        addHistory(
                histories,
                project,
                ProjectField.NAME,
                project.getOldName(),
                project.getName()
        );

        addHistory(
                histories,
                project,
                ProjectField.DESCRIPTION,
                project.getOldDescription(),
                project.getDescription()
        );

        addHistory(
                histories,
                project,
                ProjectField.STATUS,
                project.getOldStatus(),
                project.getStatus() != null
                        ? project.getStatus().name()
                        : null
        );

        if (!histories.isEmpty()) {
            repository.saveAll(histories);
        }
    }

    private void addHistory(
            List<ProjectHistory> histories,
            Project project,
            ProjectField field,
            String oldValue,
            String newValue
    ) {

        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        ProjectHistory history = new ProjectHistory();

        history.setProject(project);
        history.setFieldChanged(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);

        histories.add(history);
    }

}

