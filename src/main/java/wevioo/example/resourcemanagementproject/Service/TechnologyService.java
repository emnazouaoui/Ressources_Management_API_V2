package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;
import wevioo.example.resourcemanagementproject.Mapper.TechnologyMapper;



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

        return technologyMapper.toDTO(repository.save(t));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Technology not found: " + id);
        }
        repository.deleteById(id);
    }

//    // 📄 GET ALL (pagination-ready)
//    public Page<TechnologyDTO> getAll(int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        return repository.findAll(pageable)
//                .map(technologyMapper::toDTO);
//    }
    //  GET ALL — يتبدل : page تبدأ من 1
    public Page<TechnologyDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
        return repository.findAll(pageable).map(technologyMapper::toDTO);
    }


    // SEARCH
    public Page<TechnologyDTO> searchTechnologies(
            String name,
            Integer page,
            Integer pageSize,
            CustomSort sort
    ) {
        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        return repository.searchTechnologies(
                normalize(name),
                pageable
        ).map(technologyMapper::toDTO);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }


}
