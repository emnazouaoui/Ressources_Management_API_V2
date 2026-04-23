package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Entity.UserTechnology;
import wevioo.example.resourcemanagementproject.Enums.UserField;
import wevioo.example.resourcemanagementproject.Mapper.UserMapper;
import wevioo.example.resourcemanagementproject.Repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final UserHistoryService userHistoryService;
    private final TechnologyRepository technologyRepository;
    private final UserTechnologyRepository userTechnologyRepository;


    // CREATE
    public UserDTO create(UserDTO dto) {
        User user = userMapper.toEntity(dto);

        user.setRole(roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found")));

        user.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));

        if (dto.getManagerId() != null) {
        user.setManager(userRepository.findById(dto.getManagerId())
                  .orElseThrow(() -> new RuntimeException("Manager not found")));
       }

        return userMapper.toDTO(userRepository.save(user));
    }

    // GET ALL (pagination)
    public Page<UserDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }


    // GET BY ID
    public UserDTO getById(Long id) {
        return userMapper.toDTO(
                userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"))
        );
    }

//    // UPDATE
//    public UserDTO update(Long id, UserDTO dto) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        userMapper.updateEntityFromDTO(dto, user);
//
//        user.setRole(roleRepository.findById(dto.getRoleId())
//                .orElseThrow(() -> new RuntimeException("Role not found")));
//
//        user.setDepartment(departmentRepository.findById(dto.getDepartmentId())
//                .orElseThrow(() -> new RuntimeException("Department not found")));
//
//        if (dto.getManagerId() != null) {
//            user.setManager(userRepository.findById(dto.getManagerId())
//                    .orElseThrow(() -> new RuntimeException("Manager not found")));
//        }
//
//        return userMapper.toDTO(userRepository.save(user));
//    }
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 OLD VALUES
        String oldUsername = user.getUsername();
        String oldFirstName = user.getFirstName();
        String oldLastName = user.getLastName();
        String oldEmail = user.getEmail();
        String oldPassword = user.getPassword();
        String oldLevel = user.getLevel() != null ? user.getLevel().name() : null;
        Long oldRole = user.getRole() != null ? user.getRole().getId() : null;
        Long oldDept = user.getDepartment() != null ? user.getDepartment().getId() : null;
        Long oldManager = user.getManager() != null ? user.getManager().getId() : null;

        // ✏️ UPDATE
        userMapper.updateEntityFromDTO(dto, user);

        user.setRole(roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found")));

        user.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));

        if (dto.getManagerId() != null) {
            user.setManager(userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found")));
        } else {
            user.setManager(null);
        }

        User saved = userRepository.save(user);

        // 🔥 HISTORY LOG
        userHistoryService.saveChange(id, UserField.USERNAME, oldUsername, saved.getUsername());
        userHistoryService.saveChange(id, UserField.FIRST_NAME, oldFirstName, saved.getFirstName());
        userHistoryService.saveChange(id, UserField.LAST_NAME, oldLastName, saved.getLastName());
        userHistoryService.saveChange(id, UserField.EMAIL, oldEmail, saved.getEmail());
        userHistoryService.saveChange(id, UserField.PASSWORD, oldPassword, saved.getPassword());

        userHistoryService.saveChange(id, UserField.LEVEL,
                oldLevel,
                saved.getLevel() != null ? saved.getLevel().name() : null);

        userHistoryService.saveChange(id, UserField.ROLE,
                oldRole != null ? oldRole.toString() : null,
                dto.getRoleId().toString());

        userHistoryService.saveChange(id, UserField.DEPARTMENT,
                oldDept != null ? oldDept.toString() : null,
                dto.getDepartmentId().toString());

        userHistoryService.saveChange(id, UserField.MANAGER,
                oldManager != null ? oldManager.toString() : null,
                dto.getManagerId() != null ? dto.getManagerId().toString() : null);

        return userMapper.toDTO(saved);
    }

    // DELETE
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    // SEARCH
    public List<UserDTO> search(String keyword) {
        return userRepository.searchUsers(keyword)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public List<UserDTO> getUsersByTechnologyName(String name) {

        return userRepository.findUsersByTechnologyName(name)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

//    // 🔥 search users by technologyId
//    public List<UserDTO> getUsersByTechnology(Long techId) {
//
//        return userRepository.findUsersByTechnology(techId)
//                .stream()
//                .map(userMapper::toDTO)
//                .toList();
//    }

    public void assignTechnology(Long userId, Long techId) {

        if (userTechnologyRepository.existsByUserIdAndTechnologyId(userId, techId)) {
            throw new RuntimeException("Technology already assigned");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Technology tech = technologyRepository.findById(techId)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        UserTechnology ut = new UserTechnology();
        ut.setUser(user);
        ut.setTechnology(tech);

        userTechnologyRepository.save(ut);
    }


//    // 🔥 assign technology
//    public void assignTechnology(Long userId, Long techId) {
//
//        if (!userRepository.existsById(userId)) {
//            throw new RuntimeException("User not found");
//        }
//
//        if (!technologyRepository.existsById(techId)) {
//            throw new RuntimeException("Technology not found");
//        }
//
//        if (userTechnologyRepository.existsByUserIdAndTechnologyId(userId, techId)) {
//            throw new RuntimeException("Technology already assigned");
//        }
//
//        UserTechnology ut = new UserTechnology();
//
//        User user = new User();
//        user.setId(userId);
//
//        Technology tech = new Technology();
//        tech.setId(techId);
//
//        ut.setUser(user);
//        ut.setTechnology(tech);
//
//        userTechnologyRepository.save(ut);
//    }

    // 🔥 remove technology
    @Transactional
    public void removeTechnology(Long userId, Long techId) {
        userTechnologyRepository.deleteByUserIdAndTechnologyId(userId, techId);
    }

    // 🔥 get technologies of user
    public List<Long> getUserTechnologies(Long userId) {

        return userTechnologyRepository.findByUserId(userId)
                .stream()
                .map(ut -> ut.getTechnology().getId())
                .toList();
    }




}
