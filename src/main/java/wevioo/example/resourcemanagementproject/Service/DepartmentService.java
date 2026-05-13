package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Entity.Department;
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Mapper.DepartmentMapper;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper departmentMapper;
    private final PaginationUtil paginationUtil;      // pour pagination


    // CREATE
    public DepartmentDTO create(DepartmentDTO dto) {
        Department saved = repository.save(departmentMapper.toEntity(dto));

        return departmentMapper.toDTO(saved);
    }

//    //  GET ALL — يتبدل : page تبدأ من 1
//    public Page<DepartmentDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//        return repository.findAll(pageable).map(departmentMapper::toDTO);
//    }
//  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<DepartmentDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Department> DepartmentPage = repository.findAll(pageable);

        PaginatedResponse<DepartmentDTO> response = new PaginatedResponse<>();
        response.setContent(DepartmentPage.getContent().stream().map(departmentMapper::toDTO).toList());
        response.setPage(DepartmentPage.getNumber() + 1);
        response.setPageSize(DepartmentPage.getSize());
        response.setTotalElement(DepartmentPage.getTotalElements());
        response.setTotalPage(DepartmentPage.getTotalPages());
        return response;
    }


    // GET BY ID
    public DepartmentDTO getById(Long id) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        return departmentMapper.toDTO(d);
    }

    // UPDATE
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));

        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        d.setUpdatedDate(LocalDateTime.now());

        return departmentMapper.toDTO(repository.save(d));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found: " + id);
        }
        repository.deleteById(id);
    }


//    // SEARCH
//    public Page<DepartmentDTO> searchDepartments(
//            String name,
//            String description,
//            Integer  page,
//            Integer  pageSize,
//            CustomSort sort
//    ) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//
//        return repository.searchDepartments(
//                normalize(name),
//                normalize(description),
//                pageable
//        ).map(departmentMapper::toDTO);
//    }
    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<DepartmentDTO> searchDepartments(
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

        Page<Department> departmentPage = repository.searchDepartments(
                normalize(name), normalize(description),
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<DepartmentDTO> response = new PaginatedResponse<>();
        response.setContent(departmentPage.getContent().stream()
                .map(departmentMapper::toDTO)
                .toList());
        response.setPage(departmentPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(departmentPage.getSize());
        response.setTotalElement(departmentPage.getTotalElements());
        response.setTotalPage(departmentPage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }


}
