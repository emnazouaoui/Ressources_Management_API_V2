package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectHistory;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.ProjectField;
import wevioo.example.resourcemanagementproject.Repository.ProjectHistoryRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Component
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

//    // ← Static car JPA instancie les listeners lui-même
//    private static EntityManager entityManager;
//
//    @PersistenceContext
//    public void setEntityManager(EntityManager em) {
//        entityManager = em;
//    }
//
//    @PreUpdate
//    public void preUpdate(Project current) {
//
////        // ✅ Evict من الـ cache بش نجيب الـ old values من الـ DB
////        Project old = entityManager.find(Project.class, current.getId());
////        entityManager.detach(old);  // ← هاذا هو الـ fix !
////        entityManager.refresh(old); // ← يجيب القيم من الـ DB مباشرة
//
//        // ✅ Native query — يتجاوز الـ Hibernate cache كامل
//        Project old = (Project) entityManager
//                .createNativeQuery("SELECT * FROM project WHERE id = ?", Project.class)
//                .setParameter(1, current.getId())
//                .getSingleResult();
//
//        List<ProjectHistory> histories = new ArrayList<>();
//
//        // Compare field par field
//        addIfChanged(histories, current, old.getName(), current.getName(),
//                ProjectField.NAME);
//
//        addIfChanged(histories, current, old.getDescription(), current.getDescription(),
//                ProjectField.DESCRIPTION);
//
//        addIfChanged(histories, current,
//                old.getStatus() != null ? old.getStatus().name() : null,
//                current.getStatus() != null ? current.getStatus().name() : null,
//                ProjectField.STATUS);
//
//        addIfChanged(histories, current,
//                String.valueOf(old.getStartDate()),
//                String.valueOf(current.getStartDate()),
//                ProjectField.START_DATE);
//
//        addIfChanged(histories, current,
//                String.valueOf(old.getEndDate()),
//                String.valueOf(current.getEndDate()),
//                ProjectField.END_DATE);
//
//        addIfChanged(histories, current,
//                String.valueOf(old.getProgressPercent()),
//                String.valueOf(current.getProgressPercent()),
//                ProjectField.PROGRESS);
//
//        addIfChanged(histories, current,
//                old.getProjectManager() != null ? String.valueOf(old.getProjectManager().getId()) : null,
//                current.getProjectManager() != null ? String.valueOf(current.getProjectManager().getId()) : null,
//                ProjectField.PROJECT_MANAGER);
//
//        addIfChanged(histories, current,
//                old.getClient() != null ? String.valueOf(old.getClient().getId()) : null,
//                current.getClient() != null ? String.valueOf(current.getClient().getId()) : null,
//                ProjectField.CLIENT);
//
//        // Save all histories
//        histories.forEach(entityManager::persist);
//    }
//
//    private void addIfChanged(
//            List<ProjectHistory> histories,
//            Project project,
//            String oldVal,
//            String newVal,
//            ProjectField field) {
//
//        if (oldVal == null && newVal == null) return;
//        if (oldVal != null && oldVal.equals(newVal)) return;
//
//        ProjectHistory history = new ProjectHistory();
//        history.setProject(project);
//        history.setFieldChanged(field);
//        history.setOldValue(oldVal);
//        history.setNewValue(newVal);
//       // history.setCreatedDate(LocalDateTime.now());
//        //history.setUpdatedDate(LocalDateTime.now());
//
//        histories.add(history);
//    }
}

