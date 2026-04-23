package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Enums.UserField;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;


    public void saveChange(Long userId, UserField field, String oldVal, String newVal) {

        // ignore si no change
        if (oldVal == null && newVal == null) return;
        if (oldVal != null && oldVal.equals(newVal)) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserHistory history = new UserHistory();
        history.setUser(user);
        history.setFieldChanged(field);
        history.setOldValue(oldVal);
        history.setNewValue(newVal);

        userHistoryRepository.save(history);
    }

}
