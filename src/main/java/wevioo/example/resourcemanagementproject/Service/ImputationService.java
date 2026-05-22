package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
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
    private final PaginationUtil paginationUtil;      // pour pagination



    // CREATE
    public ImputationDTO create(ImputationDTO dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Imputation imputation = imputationMapper.ImputationDTOtoImputationEntity(dto, task, user);

        return imputationMapper.ImputationToImputationDTO(imputationRepository.save(imputation));
    }

    // GET BY ID
    public ImputationDTO getById(Long id) {
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        return imputationMapper.ImputationToImputationDTO(imputation);
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<ImputationDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Imputation> ImputationPage = imputationRepository.findAll(pageable);

        PaginatedResponse<ImputationDTO> response = new PaginatedResponse<>();
        response.setContent(ImputationPage.getContent().stream().map(imputationMapper::ImputationToImputationDTO).toList());
        response.setPage(ImputationPage.getNumber() + 1);
        response.setPageSize(ImputationPage.getSize());
        response.setTotalElement(ImputationPage.getTotalElements());
        response.setTotalPage(ImputationPage.getTotalPages());
        return response;
    }


    // UPDATE
    public ImputationDTO update(Long id, ImputationDTO dto) {
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        imputationMapper.updateImputationEntity(imputation, dto, task, user);

        imputation.setUpdatedDate(LocalDateTime.now());

        return imputationMapper.ImputationToImputationDTO(imputationRepository.save(imputation));
    }

    // DELETE
    public void delete(Long id) {
        if (!imputationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Imputation not found");
        }
        imputationRepository.deleteById(id);
    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<ImputationDTO> searchImputations(
            String comment,
            String title,
            String username,
            Long taskId,
            Long userId,
            LocalDateTime date,
            Double hours,
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

        Page<Imputation> ImputationPage = imputationRepository.searchImputations(
                normalize(comment),
                normalize(title),
                normalize(username),
                taskId,
                userId,
                date,
                hours,
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<ImputationDTO> response = new PaginatedResponse<>();
        response.setContent(ImputationPage.getContent().stream()
                .map(imputationMapper::ImputationToImputationDTO)
                .toList());
        response.setPage(ImputationPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(ImputationPage.getSize());
        response.setTotalElement(ImputationPage.getTotalElements());
        response.setTotalPage(ImputationPage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
