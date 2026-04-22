package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Mapper.UserMapper;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;

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

    // UPDATE
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntityFromDTO(dto, user);

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


}
