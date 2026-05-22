package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Entity.Role;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.RoleRepository;
import wevioo.example.resourcemanagementproject.Mapper.RoleMapper;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;
    private final RoleMapper roleMapper;
    private final PaginationUtil paginationUtil;      // pour pagination


    // CREATE
    public RoleDTO create(RoleDTO dto) {
        Role saved = repository.save(roleMapper.RoleDTOtoRoleEntity(dto));
        return roleMapper.RoleToRoleDTO(saved);
    }

    // GET BY ID
    public RoleDTO getById(Long id) {
        Role r = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));

        return roleMapper.RoleToRoleDTO(r);
    }

    // UPDATE
    public RoleDTO update(Long id, RoleDTO dto) {
        Role r = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));

        r.setName(dto.getName());
        r.setDescription(dto.getDescription());
        r.setActive(dto.getActive());
        r.setUpdatedDate(LocalDateTime.now());

        return roleMapper.RoleToRoleDTO(repository.save(r));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found: " + id);
        }
        repository.deleteById(id);
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<RoleDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Role> RolePage = repository.findAll(pageable);

        PaginatedResponse<RoleDTO> response = new PaginatedResponse<>();
        response.setContent(RolePage.getContent().stream().map(roleMapper::RoleToRoleDTO).toList());
        response.setPage(RolePage.getNumber() + 1);
        response.setPageSize(RolePage.getSize());
        response.setTotalElement(RolePage.getTotalElements());
        response.setTotalPage(RolePage.getTotalPages());
        return response;
    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<RoleDTO> searchRoles(
            String name,
            String description,
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

        Page<Role> RolePage = repository.searchRoles(
                normalize(name), normalize(description),
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<RoleDTO> response = new PaginatedResponse<>();
        response.setContent(RolePage.getContent().stream()
                .map(roleMapper::RoleToRoleDTO)
                .toList());
        response.setPage(RolePage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(RolePage.getSize());
        response.setTotalElement(RolePage.getTotalElements());
        response.setTotalPage(RolePage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }



}
