package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectHistory;
import wevioo.example.resourcemanagementproject.Enums.ProjectField;
import wevioo.example.resourcemanagementproject.Repository.ProjectHistoryRepository;

@Service
@RequiredArgsConstructor
public class ProjectHistoryService {

    private final ProjectHistoryRepository projectHistoryRepository;

    public void saveHistory(Project project, ProjectField field, String oldVal, String newVal) {

        if (oldVal == null && newVal == null) return;
        if (oldVal != null && oldVal.equals(newVal)) return;

        ProjectHistory history = new ProjectHistory();
        history.setProject(project);
        history.setFieldChanged(field);
        history.setOldValue(oldVal);
        history.setNewValue(newVal);

        projectHistoryRepository.save(history);
    }


}
