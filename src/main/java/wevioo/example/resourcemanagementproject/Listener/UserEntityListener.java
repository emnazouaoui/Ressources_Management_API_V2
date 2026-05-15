package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import org.springframework.stereotype.Component;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Enums.UserField;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@Component
public class UserEntityListener {


    @PostLoad
    public void postLoad(User user) {

        user.setOldUsername(user.getUsername());
        user.setOldFirstName(user.getFirstName());
        user.setOldLastName(user.getLastName());
        user.setOldEmail(user.getEmail());
        user.setOldPhone(user.getPhone());

        user.setOldActive(
                user.getActive() != null
                        ? user.getActive().toString()
                        : null
        );

        user.setOldLevel(
                user.getLevel() != null
                        ? user.getLevel().name()
                        : null
        );
    }

    @PreUpdate
    public void preUpdate(User user) {

        user.setUpdatedDate(LocalDateTime.now());

        UserHistoryRepository repository =
                SpringContext.getBean(UserHistoryRepository.class);

        List<UserHistory> histories = new ArrayList<>();

        addHistory(
                histories,
                user,
                UserField.USERNAME,
                user.getOldUsername(),
                user.getUsername()
        );

        addHistory(
                histories,
                user,
                UserField.FIRST_NAME,
                user.getOldFirstName(),
                user.getFirstName()
        );

        addHistory(
                histories,
                user,
                UserField.LAST_NAME,
                user.getOldLastName(),
                user.getLastName()
        );

        addHistory(
                histories,
                user,
                UserField.EMAIL,
                user.getOldEmail(),
                user.getEmail()
        );

        addHistory(
                histories,
                user,
                UserField.PHONE,
                user.getOldPhone(),
                user.getPhone()
        );

        addHistory(
                histories,
                user,
                UserField.ACTIVE,
                user.getOldActive(),
                user.getActive() != null
                        ? user.getActive().toString()
                        : null
        );

        addHistory(
                histories,
                user,
                UserField.LEVEL,
                user.getOldLevel(),
                user.getLevel() != null
                        ? user.getLevel().name()
                        : null
        );

        if (!histories.isEmpty()) {
            repository.saveAll(histories);
        }
    }

    private void addHistory(
            List<UserHistory> histories,
            User user,
            UserField field,
            String oldValue,
            String newValue
    ) {

        if (Objects.equals(oldValue, newValue)) {
            return;
        }

        UserHistory history = new UserHistory();

        history.setUser(user);
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
//    public void preUpdate(User current) {
//
////        // ✅ Evict من الـ cache بش نجيب الـ old values من الـ DB
////        User old = entityManager.find(User.class, current.getId());
////        entityManager.detach(old);  // ← هاذا هو الـ fix !
////        entityManager.refresh(old); // ← يجيب القيم من الـ DB مباشرة
//
//        // ✅ Native query — يتجاوز الـ Hibernate cache كامل
//        User old = (User) entityManager
//                .createNativeQuery("SELECT * FROM users WHERE id = ?", User.class)
//                .setParameter(1, current.getId())
//                .getSingleResult();
//
//        List<UserHistory> histories = new ArrayList<>();
//
//        addIfChanged(histories, current, old.getUsername(), current.getUsername(), UserField.USERNAME);
//        addIfChanged(histories, current, old.getFirstName(), current.getFirstName(), UserField.FIRST_NAME);
//        addIfChanged(histories, current, old.getLastName(), current.getLastName(), UserField.LAST_NAME);
//        addIfChanged(histories, current, old.getEmail(), current.getEmail(), UserField.EMAIL);
//        addIfChanged(histories, current, old.getPhone(), current.getPhone(), UserField.PHONE);
//        addIfChanged(histories, current,
//                old.getLevel() != null ? old.getLevel().name() : null,
//                current.getLevel() != null ? current.getLevel().name() : null,
//                UserField.LEVEL);
//        addIfChanged(histories, current,
//                old.getRole() != null ? String.valueOf(old.getRole().getId()) : null,
//                current.getRole() != null ? String.valueOf(current.getRole().getId()) : null,
//                UserField.ROLE);
//        addIfChanged(histories, current,
//                old.getDepartment() != null ? String.valueOf(old.getDepartment().getId()) : null,
//                current.getDepartment() != null ? String.valueOf(current.getDepartment().getId()) : null,
//                UserField.DEPARTMENT);
//
//        histories.forEach(entityManager::persist);
//    }
//
//    private void addIfChanged(List<UserHistory> histories, User user,
//                              String oldVal, String newVal, UserField field) {
//        if (oldVal == null && newVal == null) return;
//        if (oldVal != null && oldVal.equals(newVal)) return;
//
//        UserHistory history = new UserHistory();
//        history.setUser(user);
//        history.setFieldChanged(field);
//        history.setOldValue(oldVal);
//        history.setNewValue(newVal);
//        //history.setCreatedDate(LocalDateTime.now());
//        //history.setUpdatedDate(LocalDateTime.now());
//        histories.add(history);
//    }

}
