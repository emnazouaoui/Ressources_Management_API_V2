package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Repository.ImputationRepository;
import wevioo.example.resourcemanagementproject.Repository.TaskRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;
import wevioo.example.resourcemanagementproject.Mapper.ImputationMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImputationService {


    private final ImputationRepository imputationRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ImputationMapper imputationMapper;


    // CREATE
    public ImputationDTO create(ImputationDTO dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Imputation imputation = imputationMapper.toEntity(dto, task, user);

        return imputationMapper.toDTO(imputationRepository.save(imputation));
    }

    // GET BY ID
    public ImputationDTO getById(Long id) {
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imputation not found"));

        return imputationMapper.toDTO(imputation);
    }

//    // GET ALL
//    public List<ImputationDTO> getAll() {
//        return imputationRepository.findAll()
//                .stream()
//                .map(ImputationMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    // 📄 GET ALL WITH PAGINATION
    public Page<ImputationDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return imputationRepository.findAll(pageable)
                .map(imputationMapper::toDTO);
    }


    // UPDATE
    public ImputationDTO update(Long id, ImputationDTO dto) {
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imputation not found"));

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        imputationMapper.updateEntity(imputation, dto, task, user);

        return imputationMapper.toDTO(imputationRepository.save(imputation));
    }

    // DELETE
    public void delete(Long id) {
        if (!imputationRepository.existsById(id)) {
            throw new RuntimeException("Imputation not found");
        }
        imputationRepository.deleteById(id);
    }

    public Page<ImputationDTO> searchImputations(
            String comment,
            String title,
            String username,
            Long taskId,
            Long userId,
            LocalDateTime date,
            Double hours,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return imputationRepository.searchImputations(
                normalize(comment),
                normalize(title),
                normalize(username),
                taskId,
                userId,
                date,
                hours,
                pageable
        ).map(imputationMapper::toDTO);
    }
    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
