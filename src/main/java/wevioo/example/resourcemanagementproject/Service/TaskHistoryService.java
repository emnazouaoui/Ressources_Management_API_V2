package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.TaskHistory;
import wevioo.example.resourcemanagementproject.Enums.TaskField;
import wevioo.example.resourcemanagementproject.Repository.TaskHistoryRepository;


@Service
@RequiredArgsConstructor
public class TaskHistoryService {


    private final TaskHistoryRepository taskHistoryRepository;

    public void saveHistory(Task task,
                            TaskField fieldChanged,
                            String oldValue,
                            String newValue) {

        if (oldValue == null && newValue == null) return;
        if (oldValue != null && oldValue.equals(newValue)) return;

        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setFieldChanged(fieldChanged);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);

        taskHistoryRepository.save(history);
    }



}
