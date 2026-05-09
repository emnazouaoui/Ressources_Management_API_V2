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
import wevioo.example.resourcemanagementproject.Enums.Level;
import wevioo.example.resourcemanagementproject.Enums.UserField;
import wevioo.example.resourcemanagementproject.Mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

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

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // CREATE
    public UserDTO create(UserDTO dto) {
        User user = userMapper.toEntity(dto);

        // 🔥 CRYPT PASSWORD
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

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

        // 🔥 CRYPT PASSWORD ONLY IF PROVIDED
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

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
        //userHistoryService.saveChange(id, UserField.PASSWORD, oldPassword, saved.getPassword());
        userHistoryService.saveChange(id, UserField.PASSWORD,
                oldPassword != null ? "UPDATED" : null,
                dto.getPassword() != null ? "UPDATED" : null);

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


    //  SEARCH
    public Page<UserDTO> searchUsers(
            String username,
            String firstName,
            String lastName,
            String email,
            Boolean active,
            Level level,
            Long roleId,
            String roleName,
            Long departmentId,
            String departmentName,
            Long managerId,
            String managerUsername,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.searchUsers(
                normalize(username),
                normalize(firstName),
                normalize(lastName),
                normalize(email),
                active,
                level,
                roleId,
                normalize(roleName),
                departmentId,
                normalize(departmentName),
                managerId,
                normalize(managerUsername),
                pageable
        ).map(userMapper::toDTO);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
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


//    public void assignTechnology(Long userId, Long techId) {
//
//        if (userTechnologyRepository.existsByUserIdAndTechnologyId(userId, techId)) {
//            throw new RuntimeException("Technology already assigned");
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Technology tech = technologyRepository.findById(techId)
//                .orElseThrow(() -> new RuntimeException("Technology not found"));
//
//        UserTechnology ut = new UserTechnology();
//        ut.setUser(user);
//        ut.setTechnology(tech);
//
//        userTechnologyRepository.save(ut);
//    }

    // ✅ Nouvelle version
    public void assignTechnology(Long userId, Long techId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Technology tech = technologyRepository.findById(techId)
                .orElseThrow(() -> new RuntimeException("Technology not found"));

        if (user.getTechnologies().contains(tech)) {
            throw new RuntimeException("Technology already assigned");
        }

        user.getTechnologies().add(tech);
        userRepository.save(user);
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

//    // 🔥 remove technology
//    @Transactional
//    public void removeTechnology(Long userId, Long techId) {
//        userTechnologyRepository.deleteByUserIdAndTechnologyId(userId, techId);
//    }
    // ✅ Nouvelle version
    @Transactional
    public void removeTechnology(Long userId, Long techId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getTechnologies().removeIf(t -> t.getId() == techId);
        userRepository.save(user);
    }

//    // 🔥 get technologies of user
//    public List<Long> getUserTechnologies(Long userId) {
//
//        return userTechnologyRepository.findByUserId(userId)
//                .stream()
//                .map(ut -> ut.getTechnology().getId())
//                .toList();
//    }
    // ✅ Nouvelle version
    public List<Long> getUserTechnologies(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getTechnologies()
                .stream()
                .map(Technology::getId)
                .toList();
    }

    // =========================
    // 🔥 Upload photo
    // =========================


//    public String uploadPhoto(MultipartFile file) {
//
//        try {
//            String uploadDir = "uploads/";
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path path = Paths.get(uploadDir + fileName);
//            Files.createDirectories(path.getParent());
//            Files.write(path, file.getBytes());
//            return fileName;
//        } catch (IOException e) {
//            throw new RuntimeException("Error uploading file");
//        }
//    }
//
//    public UserDTO uploadUserPhoto(Long userId, MultipartFile file) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String fileName = uploadPhoto(file);
//        user.setPhoto(fileName);
//        return userMapper.toDTO(userRepository.save(user));
//    }
//
//    //-------------------------------- Get photo -------------------------//
//
//    //GET PHOTO BY USER ID
//    public Resource getUserPhoto(Long userId) {
//
//        try {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            if (user.getPhoto() == null || user.getPhoto().isBlank()) {
//                throw new RuntimeException("User has no photo");
//            }
//
//            Path filePath = Paths.get("uploads")
//                    .resolve(user.getPhoto())
//                    .normalize();
//
//            Resource resource = new UrlResource(filePath.toUri());
//
//            if (!resource.exists() || !resource.isReadable()) {
//                throw new RuntimeException("Image not found");
//            }
//
//            return resource;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error loading user photo");
//        }
//    }

}
