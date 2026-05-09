package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Entity.Role;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Mapper.RoleMapper;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper roleMapper;

    // CREATE
    public RoleDTO create(RoleDTO dto) {
        Role saved = repository.save(roleMapper.toEntity(dto));
        return roleMapper.toDTO(saved);
    }

    // GET BY ID
    public RoleDTO getById(Long id) {
        Role r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));

        return roleMapper.toDTO(r);
    }

    // UPDATE
    public RoleDTO update(Long id, RoleDTO dto) {
        Role r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found: " + id));

        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setActive(dto.getActive());

        return roleMapper.toDTO(repository.save(r));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Role not found: " + id);
        }
        repository.deleteById(id);
    }

    // 📄 GET ALL (pagination ready)
    public Page<RoleDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                .map(roleMapper::toDTO);
    }


    // SEARCH
    public Page<RoleDTO> searchRoles(
            String name,
            String description,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.searchRoles(
                normalize(name),
                normalize(description),
                pageable
        ).map(roleMapper::toDTO);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }



}
