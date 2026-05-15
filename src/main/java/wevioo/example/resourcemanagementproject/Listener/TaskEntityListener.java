package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PreUpdate;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.TaskHistory;
import wevioo.example.resourcemanagementproject.Enums.TaskField;
import wevioo.example.resourcemanagementproject.Repository.TaskHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskEntityListener {


    @PostLoad
    public void postLoad(Task task) {

        task.setOldTitle(task.getTitle());
        task.setOldDescription(task.getDescription());

        task.setOldStatus(
                task.getStatus() != null
                        ? task.getStatus().name()
                        : null
        );

        task.setOldPriority(
                task.getPriority() != null
                        ? task.getPriority().name()
                        : null
        );

        task.setOldEstimatedHours(
                task.getEstimatedHours() != null
                        ? task.getEstimatedHours().toString()
                        : null
        );

        task.setOldConsumedHours(
                task.getConsumedHours() != null
                        ? task.getConsumedHours().toString()
                        : null
        );
    }

    @PreUpdate
    public void preUpdate(Task task) {

        task.setUpdatedDate(LocalDateTime.now());

        TaskHistoryRepository repository =
                SpringContext.getBean(TaskHistoryRepository.class);

        List<TaskHistory> histories = new ArrayList<>();

        addHistory(
                histories,
                task,
                TaskField.TITLE,
                task.getOldTitle(),
                task.getTitle()
        );

        addHistory(
                histories,
                task,
                TaskField.DESCRIPTION,
                task.getOldDescription(),
                task.getDescription()
        );

        addHistory(
                histories,
                task,
                TaskField.STATUS,
                task.getOldStatus(),
                task.getStatus() != null
                        ? task.getStatus().name()
                        : null
        );

        addHistory(
                histories,
                task,
                TaskField.PRIORITY,
                task.getOldPriority(),
                task.getPriority() != null
                        ? task.getPriority().name()
                        : null
        );

        addHistory(
                histories,
                task,
                TaskField.ESTIMATED_HOURS,
                task.getOldEstimatedHours(),
                task.getEstimatedHours() != null
                        ? task.getEstimatedHours().toString()
                        : null
        );

        addHistory(
                histories,
                task,
                TaskField.CONSUMED_HOURS,
                task.getOldConsumedHours(),
                task.getConsumedHours() != null
                        ? task.getConsumedHours().toString()
                        : null
        );

        if (!histories.isEmpty()) {
            repository.saveAll(histories);
        }
    }

    private void addHistory(
            List<TaskHistory> histories,
            Task task,
            TaskField field,
            String oldValue,
            String newValue
    ) {

        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        TaskHistory history = new TaskHistory();

        history.setTask(task);
        history.setFieldChanged(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);

        histories.add(history);
    }

}
