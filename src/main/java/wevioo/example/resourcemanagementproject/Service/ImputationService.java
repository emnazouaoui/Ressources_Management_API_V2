package wevioo.example.resourcemanagementproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Config.SecurityUtils;
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
import wevioo.example.resourcemanagementproject.Validator.Impl.ImputationValidator;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ImputationService {


    private final ImputationRepository imputationRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ImputationMapper imputationMapper;
    private final PaginationUtil paginationUtil;      // pour pagination
    private final SecurityUtils securityUtils;
    private final ImputationValidator imputationValidator;  // ← inject


    // CREATE
    public ImputationDTO create(ImputationDTO dto) {
        imputationValidator.validateCreate(dto);

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User user;

        //  User → force userId = lui-même
        if (securityUtils.isUser()) {
            user = userRepository.findById(securityUtils.getCurrentUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } else {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        Imputation imputation = imputationMapper.ImputationDTOtoImputationEntity(dto, task, user);

        return imputationMapper.ImputationToImputationDTO(imputationRepository.save(imputation));
    }

    // GET BY ID
    public ImputationDTO getById(Long id) {
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        User currentUser = securityUtils.getCurrentUser();

        // User → seulement ses propres
        if (securityUtils.isUser() && imputation.getUser().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

        // Manager → imputations de ses projects
        if (securityUtils.isManager() &&
                imputation.getTask() != null &&
                imputation.getTask().getProject() != null &&
                imputation.getTask().getProject().getProjectManager().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

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

        //Page<Imputation> ImputationPage = imputationRepository.findAll(pageable);
        User currentUser = securityUtils.getCurrentUser();
        Page<Imputation> imputationPage;

        if (securityUtils.isAdmin()) {
            //  Admin → toutes
            imputationPage = imputationRepository.findAll(pageable);

        } else if (securityUtils.isManager()) {
            //  Manager → imputations des tasks de ses projects
            imputationPage = imputationRepository
                    .findByTask_Project_ProjectManagerId(currentUser.getId(), pageable);

        } else {
            //  User → seulement ses propres imputations
            imputationPage = imputationRepository
                    .findByUserId(currentUser.getId(), pageable);
        }

        PaginatedResponse<ImputationDTO> response = new PaginatedResponse<>();
        response.setContent(imputationPage.getContent().stream().map(imputationMapper::ImputationToImputationDTO).toList());
        response.setPage(imputationPage.getNumber() + 1);
        response.setPageSize(imputationPage.getSize());
        response.setTotalElement(imputationPage.getTotalElements());
        response.setTotalPage(imputationPage.getTotalPages());
        return response;
    }


    // UPDATE
    public ImputationDTO update(Long id, ImputationDTO dto) {
        imputationValidator.validateUpdate(dto);

        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        User currentUser = securityUtils.getCurrentUser();

        //  User → seulement ses propres imputations
        if (securityUtils.isUser() && imputation.getUser().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

        //  Manager → imputations de ses projects
        if (securityUtils.isManager() &&
                imputation.getTask() != null &&
                imputation.getTask().getProject() != null &&
                imputation.getTask().getProject().getProjectManager().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

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
//        if (!imputationRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Imputation not found");
//        }
//        imputationRepository.deleteById(id);
        Imputation imputation = imputationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        User currentUser = securityUtils.getCurrentUser();

        //  User → seulement ses propres
        if (securityUtils.isUser() && imputation.getUser().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

        //  Manager → imputations de ses projects
        if (securityUtils.isManager() &&
                imputation.getTask() != null &&
                imputation.getTask().getProject() != null &&
                imputation.getTask().getProject().getProjectManager().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Imputation not found");
        }

        imputationRepository.deleteById(id);
    }

    //  SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<ImputationDTO> searchImputations(String comment, String title, String username,
            Long taskId, Long userId, LocalDateTime date, Double hours, Integer page, Integer pageSize, String sortBy, String sortDir
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

//        Page<Imputation> ImputationPage = imputationRepository.searchImputations(normalize(comment),
//                normalize(title), normalize(username), taskId, userId, date, hours, pageable
//        );
        User currentUser = securityUtils.getCurrentUser();
        Page<Imputation> imputationPage;

        if (securityUtils.isAdmin()) {
            imputationPage = imputationRepository.searchImputations(
                    normalize(comment), normalize(title), normalize(username),
                    taskId, userId, date, hours, pageable
            );
        } else if (securityUtils.isManager()) {
            imputationPage = imputationRepository
                    .findByTask_Project_ProjectManagerId(currentUser.getId(), pageable);
        } else {
            //  User → force userId = lui-même
            imputationPage = imputationRepository
                    .findByUserId(currentUser.getId(), pageable);
        }

        // ← البناء الجديد للـ response
        PaginatedResponse<ImputationDTO> response = new PaginatedResponse<>();
        response.setContent(imputationPage.getContent().stream()
                .map(imputationMapper::ImputationToImputationDTO)
                .toList());
        response.setPage(imputationPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(imputationPage.getSize());
        response.setTotalElement(imputationPage.getTotalElements());
        response.setTotalPage(imputationPage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}
