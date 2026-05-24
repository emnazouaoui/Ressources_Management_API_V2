package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.Level;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;
import wevioo.example.resourcemanagementproject.Mapper.UserMapper;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final TechnologyRepository technologyRepository;
    private final PaginationUtil paginationUtil;      // pour pagination

    private final PasswordEncoder passwordEncoder;
    //private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // CREATE
    public UserDTO create(UserDTO dto) {
        User user = userMapper.UserDTOtoUserEntity(dto);

        // 🔥 CRYPT PASSWORD
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));

        user.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));

        if (dto.getManagerId() != null) {
        user.setManager(userRepository.findById(dto.getManagerId())
                  .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
       }

        return userMapper.UserToUserDTO(userRepository.save(user));
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<UserDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<User> UserPage = userRepository.findAll(pageable);

        PaginatedResponse<UserDTO> response = new PaginatedResponse<>();
        response.setContent(UserPage.getContent().stream().map(userMapper::UserToUserDTO).toList());
        response.setPage(UserPage.getNumber() + 1);
        response.setPageSize(UserPage.getSize());
        response.setTotalElement(UserPage.getTotalElements());
        response.setTotalPage(UserPage.getTotalPages());
        return response;
    }


    // GET BY ID
    public UserDTO getById(Long id) {
        return userMapper.UserToUserDTO(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
        );
    }


    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ✅ @PreUpdate يتكفل بالـ history تلقائياً — supprime tout le bloc HISTORY

        userMapper.updateUserEntityFromUserDTO(dto, user);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setRole(roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));

        user.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));

        if (dto.getManagerId() != null) {
            user.setManager(userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));
        } else {
            user.setManager(null);
        }

        return userMapper.UserToUserDTO(userRepository.save(user));
    }

    // DELETE
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }


    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<UserDTO> searchUsers(
            String username,
            String firstName,
            String lastName,
            String email,
            Boolean active,
            Level level,
            String phone,
            Long roleId,
            String roleName,
            Long departmentId,
            String departmentName,
            Long managerId,
            String managerUsername,
            Integer page,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        // ← بدل Sort.by(sortBy).ascending() مباشرة
        // نبني CustomSort ونمرروه لـ PaginationUtil بش يvalidiha
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(
                customSort,
                Sort.Direction.ASC,
                "createdDate"                  // ← default si sort == null
        );

        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<User> UserPage = userRepository.searchUsers(
                normalize(username),
                normalize(firstName),
                normalize(lastName),
                normalize(email),
                active,
                level,
                normalize(phone),
                roleId,
                normalize(roleName),
                departmentId,
                normalize(departmentName),
                managerId,
                normalize(managerUsername),
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<UserDTO> response = new PaginatedResponse<>();
        response.setContent(UserPage.getContent().stream()
                .map(userMapper::UserToUserDTO)
                .toList());
        response.setPage(UserPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(UserPage.getSize());
        response.setTotalElement(UserPage.getTotalElements());
        response.setTotalPage(UserPage.getTotalPages());

        return response;
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    public List<UserDTO> getUsersByTechnologyName(String name) {

        return userRepository.findUsersByTechnologyName(name)
                .stream()
                .map(userMapper::UserToUserDTO)
                .toList();
    }


    // assign technology to user
    public void assignTechnology(Long userId, Long techId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Technology tech = technologyRepository.findById(techId)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        if (user.getTechnologies().contains(tech)) {
            throw new ResourceNotFoundException("Technology already assigned");
        }

        user.getTechnologies().add(tech);
        userRepository.save(user);
    }


    // ✅ remove technology
    @Transactional
    public void removeTechnology(Long userId, Long techId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.getTechnologies().removeIf(t -> t.getId() == techId);
        userRepository.save(user);
    }

    //  Get technology by user
    public List<Long> getUserTechnologies(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
