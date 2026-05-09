package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Entity.Department;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;
import wevioo.example.resourcemanagementproject.Mapper.DepartmentMapper;



import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper departmentMapper;

    // CREATE
    public DepartmentDTO create(DepartmentDTO dto) {
        Department saved = repository.save(departmentMapper.toEntity(dto));

        return departmentMapper.toDTO(saved);
    }

//    // GET ALL
//    public List<DepartmentDTO> getAll() {
//        return departmentMapper.toDtoList(repository.findAll());
//    }

    // 📄 GET ALL WITH PAGINATION
    public Page<DepartmentDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                   .map(departmentMapper::toDTO);
    }


    // GET BY ID
    public DepartmentDTO getById(Long id) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        return departmentMapper.toDTO(d);
    }

    // UPDATE
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        d.setName(dto.getName());
        d.setDescription(dto.getDescription());

        return departmentMapper.toDTO(repository.save(d));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        repository.deleteById(id);
    }


    // SEARCH
    public Page<DepartmentDTO> searchDepartments(
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

        return repository.searchDepartments(
                normalize(name),
                normalize(description),
                pageable
        ).map(departmentMapper::toDTO);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }


}
