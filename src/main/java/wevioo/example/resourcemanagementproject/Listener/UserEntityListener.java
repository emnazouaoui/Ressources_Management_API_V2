package wevioo.example.resourcemanagementproject.Listener;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PreUpdate;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Enums.UserField;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

}
