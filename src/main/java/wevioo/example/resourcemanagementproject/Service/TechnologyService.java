package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Mapper.TechnologyMapper;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository repository;

    // CREATE
    public TechnologyDTO create(TechnologyDTO dto) {
        Technology saved = repository.save(TechnologyMapper.toEntity(dto));
        return TechnologyMapper.toDto(saved);
    }

    // GET BY ID
    public TechnologyDTO getById(Long id) {
        Technology t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found: " + id));

        return TechnologyMapper.toDto(t);
    }

    // UPDATE
    public TechnologyDTO update(Long id, TechnologyDTO dto) {
        Technology t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technology not found: " + id));

        t.setName(dto.getName());

        return TechnologyMapper.toDto(repository.save(t));
    }

    // DELETE
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Technology not found: " + id);
        }
        repository.deleteById(id);
    }

    // 📄 GET ALL (pagination-ready)
    public Page<TechnologyDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                .map(TechnologyMapper::toDto);
    }


    // 🔎 SEARCH
    public List<TechnologyDTO> search(String keyword) {
        return TechnologyMapper.toDtoList(repository.searchByKeyword(keyword));
    }
}
