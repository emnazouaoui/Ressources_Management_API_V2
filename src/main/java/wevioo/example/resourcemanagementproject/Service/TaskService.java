package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Entity.Client;
import wevioo.example.resourcemanagementproject.Entity.Department;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskField;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ImputationRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.TaskRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;
import wevioo.example.resourcemanagementproject.Mapper.TaskMapper;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
   // private final TaskHistoryService taskHistoryService;
    private final ImputationRepository imputationRepository;
    private final TaskMapper taskMapper;
    private final PaginationUtil paginationUtil;      // pour pagination




    // ================= CREATE =================
    public TaskDTO create(TaskDTO dto) {

        Task task = taskMapper.toEntity(dto);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User user = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        task.setProject(project);
        task.setAssignedUser(user);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    // ================= GET BY ID =================
    public TaskDTO getById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return taskMapper.toDTO(task);
    }

//    // ================= UPDATE =================
//    public TaskDTO update(Long id, TaskDTO dto) {
//
//        Task task = taskRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Task not found"));
//
//        task.setTitle(dto.getTitle());
//        task.setDescription(dto.getDescription());
//        task.setStatus(dto.getStatus());
//        task.setPriority(dto.getPriority());
//        task.setStartDate(dto.getStartDate());
//        task.setEndDate(dto.getEndDate());
//        task.setEstimatedHours(dto.getEstimatedHours());
//        task.setConsumedHours(dto.getConsumedHours());
//
//        if (dto.getProjectId() != null) {
//            Project project = projectRepository.findById(dto.getProjectId())
//                    .orElseThrow(() -> new RuntimeException("Project not found"));
//            task.setProject(project);
//        }
//
//        if (dto.getAssignedUserId() != null) {
//            User user = userRepository.findById(dto.getAssignedUserId())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//            task.setAssignedUser(user);
//        }
//
//        return TaskMapper.toDTO(taskRepository.save(task));
//    }

//    public TaskDTO update(Long id, TaskDTO dto) {
//
//        Task task = taskRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
//
//        // ================= HISTORY TRACKING =================
//
//        taskHistoryService.saveHistory(task,
//                TaskField.TITLE,
//                task.getTitle(),
//                dto.getTitle());
//
//        taskHistoryService.saveHistory(task,
//                TaskField.DESCRIPTION,
//                task.getDescription(),
//                dto.getDescription());
//
//        taskHistoryService.saveHistory(task,
//                TaskField.STATUS,
//                task.getStatus() != null ? task.getStatus().toString() : null,
//                dto.getStatus() != null ? dto.getStatus().toString() : null);
//
//        taskHistoryService.saveHistory(task,
//                TaskField.PRIORITY,
//                task.getPriority() != null ? task.getPriority().toString() : null,
//                dto.getPriority() != null ? dto.getPriority().toString() : null);
//
//        taskHistoryService.saveHistory(task,
//                TaskField.START_DATE,
//                task.getStartDate() != null ? task.getStartDate().toString() : null,
//                dto.getStartDate() != null ? dto.getStartDate().toString() : null);
//
//        taskHistoryService.saveHistory(task,
//                TaskField.END_DATE,
//                task.getEndDate() != null ? task.getEndDate().toString() : null,
//                dto.getEndDate() != null ? dto.getEndDate().toString() : null);
//
//        taskHistoryService.saveHistory(task,
//                TaskField.ESTIMATED_HOURS,
//                task.getEstimatedHours() != null ? task.getEstimatedHours().toString() : null,
//                dto.getEstimatedHours() != null ? dto.getEstimatedHours().toString() : null);
//
//        taskHistoryService.saveHistory(task,
//                TaskField.CONSUMED_HOURS,
//                task.getConsumedHours() != null ? task.getConsumedHours().toString() : null,
//                dto.getConsumedHours() != null ? dto.getConsumedHours().toString() : null);
//
//        // ================= UPDATE VALUES =================
//
//        task.setTitle(dto.getTitle());
//        task.setDescription(dto.getDescription());
//        task.setStatus(dto.getStatus());
//        task.setPriority(dto.getPriority());
//        task.setStartDate(dto.getStartDate());
//        task.setEndDate(dto.getEndDate());
//        task.setEstimatedHours(dto.getEstimatedHours());
//        task.setConsumedHours(dto.getConsumedHours());
//        task.setUpdatedDate(LocalDateTime.now());
//
//        if (dto.getProjectId() != null) {
//            Project project = projectRepository.findById(dto.getProjectId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
//            task.setProject(project);
//        }
//
//        if (dto.getAssignedUserId() != null) {
//            User user = userRepository.findById(dto.getAssignedUserId())
//                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//            task.setAssignedUser(user);
//        }
//
//        return taskMapper.toDTO(taskRepository.save(task));
//    }
public TaskDTO update(Long id, TaskDTO dto) {

    Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

    // ✅ @PreUpdate يتكفل بالـ history تلقائياً — supprime tout le bloc HISTORY

    task.setTitle(dto.getTitle());
    task.setDescription(dto.getDescription());
    task.setStatus(dto.getStatus());
    task.setPriority(dto.getPriority());
    task.setStartDate(dto.getStartDate());
    task.setEndDate(dto.getEndDate());
    task.setEstimatedHours(dto.getEstimatedHours());
    task.setConsumedHours(dto.getConsumedHours());

    if (dto.getProjectId() != null) {
        task.setProject(projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found")));
    }

    if (dto.getAssignedUserId() != null) {
        task.setAssignedUser(userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    // ✅ @PostUpdate يتكالى تلقائياً بعد save
    return taskMapper.toDTO(taskRepository.save(task));
}

    // ================= DELETE =================
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    // ================= GET ALL TASKS =================
//    //  GET ALL — يتبدل : page تبدأ من 1
//    public Page<TaskDTO> getAllTasks(Integer page, Integer pageSize, CustomSort sort) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//        return taskRepository.findAll(pageable).map(taskMapper::toDTO);
//    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<TaskDTO> getAllTasks(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Task> TaskPage = taskRepository.findAll(pageable);

        PaginatedResponse<TaskDTO> response = new PaginatedResponse<>();
        response.setContent(TaskPage.getContent().stream().map(taskMapper::toDTO).toList());
        response.setPage(TaskPage.getNumber() + 1);
        response.setPageSize(TaskPage.getSize());
        response.setTotalElement(TaskPage.getTotalElements());
        response.setTotalPage(TaskPage.getTotalPages());
        return response;
    }

//    //  SEARCH
//    public Page<TaskDTO> searchTasks(
//            String title,
//            String description,
//            TaskStatus status,
//            Priority priority,
//            Long projectId,
//            String projectName,
//            Long assignedUserId,
//            String assignedUserUsername,
//            LocalDateTime startDate,
//            LocalDateTime endDate,
//            Double estimatedHours,
//            Double consumedHours,
//            Integer page,
//            Integer pageSize,
//            CustomSort sort
//    ) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//
//        return taskRepository.searchTasks(
//                normalize(title),
//                normalize(description),
//                status,
//                priority,
//                projectId,
//                normalize(projectName),
//                assignedUserId,
//                normalize(assignedUserUsername),
//                startDate,
//                endDate,
//                estimatedHours,
//                consumedHours,
//                pageable
//        ).map(taskMapper::toDTO);
//    }
    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<TaskDTO> searchTasks(
            String title,
            String description,
            TaskStatus status,
            Priority priority,
            Long projectId,
            String projectName,
            Long assignedUserId,
            String assignedUserUsername,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Double estimatedHours,
            Double consumedHours,
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

        Page<Task> TaskPage = taskRepository.searchTasks(
                normalize(title),
                normalize(description),
                status,
                priority,
                projectId,
                normalize(projectName),
                assignedUserId,
                normalize(assignedUserUsername),
                startDate,
                endDate,
                estimatedHours,
                consumedHours,
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<TaskDTO> response = new PaginatedResponse<>();
        response.setContent(TaskPage.getContent().stream()
                .map(taskMapper::toDTO)
                .toList());
        response.setPage(TaskPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(TaskPage.getSize());
        response.setTotalElement(TaskPage.getTotalElements());
        response.setTotalPage(TaskPage.getTotalPages());

        return response;
    }

        // -------------------------------------------------------------------------
        // Helpers
        // -------------------------------------------------------------------------
        private String normalize(String value) {
            return (value == null || value.isBlank()) ? null : value.trim();
        }

    // ================= Relations =================


    //  ADD IMPUTATION TO TASK
    public TaskDTO addImputationToTask(Long taskId, Long imputationId) {

        Task task = taskRepository.findByIdWithImputations(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Imputation imputation = imputationRepository.findById(imputationId)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        // ربط
        imputation.setTask(task);
        task.getImputations().add(imputation);

        // recalcul consumedHours
        recalculateConsumedHours(task);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    // ➖ REMOVE IMPUTATION FROM TASK
    public TaskDTO removeImputationFromTask(Long taskId, Long imputationId) {

        Task task = taskRepository.findByIdWithImputations(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Imputation imputation = imputationRepository.findById(imputationId)
                .orElseThrow(() -> new ResourceNotFoundException("Imputation not found"));

        // حذف
        task.getImputations().remove(imputation);
        imputation.setTask(null);

        recalculateConsumedHours(task);

        return taskMapper.toDTO(taskRepository.save(task));
    }

    // 🔥 recalcul propre
    private void recalculateConsumedHours(Task task) {
        Double total = task.getImputations()
                .stream()
                .map(Imputation::getHours)
                .reduce(0.0, Double::sum);

        task.setConsumedHours(total);
    }


}
