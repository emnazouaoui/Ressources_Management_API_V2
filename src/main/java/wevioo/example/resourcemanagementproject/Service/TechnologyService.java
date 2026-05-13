package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Entity.Role;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;
import wevioo.example.resourcemanagementproject.Mapper.TechnologyMapper;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository repository;
    private final TechnologyMapper technologyMapper;
    private final PaginationUtil paginationUtil;      // pour pagination



    // CREATE
    public TechnologyDTO create(TechnologyDTO dto) {
        Technology saved = repository.save(technologyMapper.toEntity(dto));
        return technologyMapper.toDTO(saved);
    }

    // GET BY ID
    public TechnologyDTO getById(Long id) {
        Technology t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found: " + id));

        return technologyMapper.toDTO(t);
    }

    // UPDATE
    public TechnologyDTO update(Long id, TechnologyDTO dto) {
        Technology t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found: " + id));

        t.setName(dto.getName());
        t.setUpdatedDate(LocalDateTime.now());


        return technologyMapper.toDTO(repository.save(t));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Technology not found: " + id);
        }
        repository.deleteById(id);
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public Page<TechnologyDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
        return repository.findAll(pageable).map(technologyMapper::toDTO);
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<TechnologyDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Technology> TechnologyPage = repository.findAll(pageable);

        PaginatedResponse<TechnologyDTO> response = new PaginatedResponse<>();
        response.setContent(TechnologyPage.getContent().stream().map(technologyMapper::toDTO).toList());
        response.setPage(TechnologyPage.getNumber() + 1);
        response.setPageSize(TechnologyPage.getSize());
        response.setTotalElement(TechnologyPage.getTotalElements());
        response.setTotalPage(TechnologyPage.getTotalPages());
        return response;
    }


//    // SEARCH
//    public Page<TechnologyDTO> searchTechnologies(
//            String name,
//            Integer page,
//            Integer pageSize,
//            CustomSort sort
//    ) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//
//        return repository.searchTechnologies(
//                normalize(name),
//                pageable
//        ).map(technologyMapper::toDTO);
//    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<TechnologyDTO> searchTechnologies(
            String name,
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

        Page<Technology> TechnologyPage = repository.searchTechnologies(
                normalize(name), pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<TechnologyDTO> response = new PaginatedResponse<>();
        response.setContent(TechnologyPage.getContent().stream()
                .map(technologyMapper::toDTO)
                .toList());
        response.setPage(TechnologyPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(TechnologyPage.getSize());
        response.setTotalElement(TechnologyPage.getTotalElements());
        response.setTotalPage(TechnologyPage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }


}
