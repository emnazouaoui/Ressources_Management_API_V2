package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Entity.LeaveRequest;
import wevioo.example.resourcemanagementproject.Mapper.LeaveRequestMapper;
import wevioo.example.resourcemanagementproject.Repository.LeaveRequestRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository repository;
    private final LeaveRequestMapper mapper;
    private final UserRepository userRepository;

    // CREATE
    public LeaveRequestDTO create(LeaveRequestDTO dto) {

        LeaveRequest lr = new LeaveRequest();
        mapper.toEntity(dto, lr);

        lr.setUser(userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));

        lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found")));

        return mapper.toDTO(repository.save(lr));
    }

    // UPDATE
    public LeaveRequestDTO update(Long id, LeaveRequestDTO dto) {

        LeaveRequest lr = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));

        mapper.toEntity(dto, lr);

        if (dto.getUserId() != null) {
            lr.setUser(userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found")));
        }

        if (dto.getProjectManagerId() != null) {
            lr.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found")));
        }

        return mapper.toDTO(repository.save(lr));
    }

    // GET ALL
    public Page<LeaveRequestDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }


    // GET BY ID
    public LeaveRequestDTO getById(Long id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("LeaveRequest not found"))
        );
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // SEARCH
    public List<LeaveRequestDTO> search(String keyword) {
        return repository.search(keyword)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


}
