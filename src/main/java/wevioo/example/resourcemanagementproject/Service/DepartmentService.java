package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Entity.Department;
import wevioo.example.resourcemanagementproject.Mapper.DepartmentMapper;
import wevioo.example.resourcemanagementproject.Repository.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;

    // CREATE
    public DepartmentDTO create(DepartmentDTO dto) {
        Department saved = repository.save(DepartmentMapper.toEntity(dto));
        return DepartmentMapper.toDto(saved);
    }

    // GET ALL
    public List<DepartmentDTO> getAll() {
        return DepartmentMapper.toDtoList(repository.findAll());
    }

    // 📄 GET ALL WITH PAGINATION
    public Page<DepartmentDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                   .map(DepartmentMapper::toDto);
    }


    // GET BY ID
    public DepartmentDTO getById(Long id) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        return DepartmentMapper.toDto(d);
    }

    // UPDATE
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));

        d.setName(dto.getName());
        d.setDescription(dto.getDescription());

        return DepartmentMapper.toDto(repository.save(d));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        repository.deleteById(id);
    }

    // SEARCH (JPQL query)
    public List<DepartmentDTO> search(String keyword) {
        return DepartmentMapper.toDtoList(repository.searchByKeyword(keyword));
    }


}
