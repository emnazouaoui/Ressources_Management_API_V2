package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.TaskHistory;
import wevioo.example.resourcemanagementproject.Enums.TaskField;
import wevioo.example.resourcemanagementproject.Repository.TaskHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Component
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


//    private static EntityManager entityManager;
//
//    @PersistenceContext
//    public void setEntityManager(EntityManager em) {
//        entityManager = em;
//    }
//
//    @PreUpdate
//    public void preUpdate(Task current) {
//
////        // ✅ Evict من الـ cache بش نجيب الـ old values من الـ DB
////        Task old = entityManager.find(Task.class, current.getId());
////        entityManager.detach(old);  // ← هاذا هو الـ fix !
////        entityManager.refresh(old); // ← يجيب القيم من الـ DB مباشرة
//        // ✅ Native query — يتجاوز الـ Hibernate cache كامل
//        Task old = (Task) entityManager
//                .createNativeQuery("SELECT * FROM task WHERE id = ?", Task.class)
//                .setParameter(1, current.getId())
//                .getSingleResult();
//
//        List<TaskHistory> histories = new ArrayList<>();
//
//        addIfChanged(histories, current, old.getTitle(), current.getTitle(), TaskField.TITLE);
//        addIfChanged(histories, current, old.getDescription(), current.getDescription(), TaskField.DESCRIPTION);
//        addIfChanged(histories, current,
//                old.getStatus() != null ? old.getStatus().name() : null,
//                current.getStatus() != null ? current.getStatus().name() : null,
//                TaskField.STATUS);
//        addIfChanged(histories, current,
//                old.getPriority() != null ? old.getPriority().name() : null,
//                current.getPriority() != null ? current.getPriority().name() : null,
//                TaskField.PRIORITY);
//        addIfChanged(histories, current,
//                old.getStartDate() != null ? old.getStartDate().toString() : null,
//                current.getStartDate() != null ? current.getStartDate().toString() : null,
//                TaskField.START_DATE);
//        addIfChanged(histories, current,
//                old.getEndDate() != null ? old.getEndDate().toString() : null,
//                current.getEndDate() != null ? current.getEndDate().toString() : null,
//                TaskField.END_DATE);
//        addIfChanged(histories, current,
//                old.getAssignedUser() != null ? String.valueOf(old.getAssignedUser().getId()) : null,
//                current.getAssignedUser() != null ? String.valueOf(current.getAssignedUser().getId()) : null,
//                TaskField.ASSIGNED_USER);
//
//        histories.forEach(entityManager::persist);
//    }
//
//    private void addIfChanged(List<TaskHistory> histories, Task task,
//                              String oldVal, String newVal, TaskField field) {
//        if (oldVal == null && newVal == null) return;
//        if (oldVal != null && oldVal.equals(newVal)) return;
//
//        TaskHistory history = new TaskHistory();
//        history.setTask(task);
//        history.setFieldChanged(field);
//        history.setOldValue(oldVal);
//        history.setNewValue(newVal);
//        //history.setCreatedDate(LocalDateTime.now());
//        //history.setUpdatedDate(LocalDateTime.now());
//        histories.add(history);
//    }

////  @PostUpdate — بعد الـ save للـ DB
//@PostUpdate
//public void postUpdate(Task current) {
//
//    ApplicationContext ctx = ApplicationContextProvider.getContext();
//    if (ctx == null) return;
//
//    TaskHistoryRepository historyRepository =
//            ctx.getBean(TaskHistoryRepository.class);
//
//    List<TaskHistory> histories = new ArrayList<>();
//
//    // ✅ يستعمل الـ @Transient fields اللي حفظناهم قبل
//    addIfChanged(histories, current,
//            current.get_oldTitle(), current.getTitle(), TaskField.TITLE);
//
//    addIfChanged(histories, current,
//            current.get_oldDescription(), current.getDescription(), TaskField.DESCRIPTION);
//
//    addIfChanged(histories, current,
//            current.get_oldStatus(),
//            current.getStatus() != null ? current.getStatus().name() : null,
//            TaskField.STATUS);
//
//    addIfChanged(histories, current,
//            current.get_oldPriority(),
//            current.getPriority() != null ? current.getPriority().name() : null,
//            TaskField.PRIORITY);
//
//    addIfChanged(histories, current,
//            current.get_oldStartDate(),
//            current.getStartDate() != null ? current.getStartDate().toString() : null,
//            TaskField.START_DATE);
//
//    addIfChanged(histories, current,
//            current.get_oldEndDate(),
//            current.getEndDate() != null ? current.getEndDate().toString() : null,
//            TaskField.END_DATE);
//
//    addIfChanged(histories, current,
//            current.get_oldAssignedUser(),
//            current.getAssignedUser() != null ? String.valueOf(current.getAssignedUser().getId()) : null,
//            TaskField.ASSIGNED_USER);
//
//    if (!histories.isEmpty()) {
//        historyRepository.saveAll(histories);
//    }
//}
//
//    private void addIfChanged(
//            List<TaskHistory> histories,
//            Task task,
//            String oldVal,
//            String newVal,
//            TaskField field) {
//
//        if (oldVal == null && newVal == null) return;
//        if (oldVal != null && oldVal.equals(newVal)) return;
//
//        TaskHistory history = new TaskHistory();
//        history.setTask(task);
//        history.setFieldChanged(field);
//        history.setOldValue(oldVal);
//        history.setNewValue(newVal);
//        histories.add(history);
//    }


}
