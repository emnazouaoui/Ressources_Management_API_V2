package wevioo.example.resourcemanagementproject.Mapper;


import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.User;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword()) // (اختياري حسب security)
                .active(user.getActive())
                .level(user.getLevel())
                .roleId(user.getRole() != null ? user.getRole().getId() : null)
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .managerId(user.getManager() != null ? user.getManager().getId() : null)
                .technologyIds(
                        user.getUsersTechnologyList() != null ?
                                user.getUsersTechnologyList().stream()
                                        .map(ut -> ut.getTechnology().getId())
                                        .toList()
                                : null
                )
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 🔐
        user.setActive(dto.getActive());
        user.setLevel(dto.getLevel());

        return user;
    }

    public void updateEntityFromDTO(UserDTO dto, User user) {
        if (dto == null || user == null) return;

        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 🔐
        user.setActive(dto.getActive());
        user.setLevel(dto.getLevel());
    }

}
