package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Entity.Imputation;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskField;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;
import wevioo.example.resourcemanagementproject.Mapper.TaskMapper;
import wevioo.example.resourcemanagementproject.Repository.ImputationRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.TaskRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskHistoryService taskHistoryService;
    private final ImputationRepository imputationRepository;


    // ================= CREATE =================
    public TaskDTO create(TaskDTO dto) {

        Task task = TaskMapper.toEntity(dto);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User user = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setProject(project);
        task.setAssignedUser(user);

        return TaskMapper.toDTO(taskRepository.save(task));
    }

    // ================= GET BY ID =================
    public TaskDTO getById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return TaskMapper.toDTO(task);
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

    public TaskDTO update(Long id, TaskDTO dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // ================= HISTORY TRACKING =================

        taskHistoryService.saveHistory(task,
                TaskField.TITLE,
                task.getTitle(),
                dto.getTitle());

        taskHistoryService.saveHistory(task,
                TaskField.DESCRIPTION,
                task.getDescription(),
                dto.getDescription());

        taskHistoryService.saveHistory(task,
                TaskField.STATUS,
                task.getStatus() != null ? task.getStatus().toString() : null,
                dto.getStatus() != null ? dto.getStatus().toString() : null);

        taskHistoryService.saveHistory(task,
                TaskField.PRIORITY,
                task.getPriority() != null ? task.getPriority().toString() : null,
                dto.getPriority() != null ? dto.getPriority().toString() : null);

        taskHistoryService.saveHistory(task,
                TaskField.START_DATE,
                task.getStartDate() != null ? task.getStartDate().toString() : null,
                dto.getStartDate() != null ? dto.getStartDate().toString() : null);

        taskHistoryService.saveHistory(task,
                TaskField.END_DATE,
                task.getEndDate() != null ? task.getEndDate().toString() : null,
                dto.getEndDate() != null ? dto.getEndDate().toString() : null);

        taskHistoryService.saveHistory(task,
                TaskField.ESTIMATED_HOURS,
                task.getEstimatedHours() != null ? task.getEstimatedHours().toString() : null,
                dto.getEstimatedHours() != null ? dto.getEstimatedHours().toString() : null);

        taskHistoryService.saveHistory(task,
                TaskField.CONSUMED_HOURS,
                task.getConsumedHours() != null ? task.getConsumedHours().toString() : null,
                dto.getConsumedHours() != null ? dto.getConsumedHours().toString() : null);

        // ================= UPDATE VALUES =================

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setEstimatedHours(dto.getEstimatedHours());
        task.setConsumedHours(dto.getConsumedHours());

        if (dto.getProjectId() != null) {
            Project project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }

        if (dto.getAssignedUserId() != null) {
            User user = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedUser(user);
        }

        return TaskMapper.toDTO(taskRepository.save(task));
    }

    // ================= DELETE =================
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    // ================= GET ALL TASKS =================
    public Page<TaskDTO> getAllTasks(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks.map(TaskMapper::toDTO);
    }

    //  SEARCH
    public Page<TaskDTO> searchTasks(
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
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return taskRepository.searchTasks(
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
        ).map(TaskMapper::toDTO);
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
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Imputation imputation = imputationRepository.findById(imputationId)
                .orElseThrow(() -> new RuntimeException("Imputation not found"));

        // ربط
        imputation.setTask(task);
        task.getImputations().add(imputation);

        // recalcul consumedHours
        recalculateConsumedHours(task);

        return TaskMapper.toDTO(taskRepository.save(task));
    }

    // ➖ REMOVE IMPUTATION FROM TASK
    public TaskDTO removeImputationFromTask(Long taskId, Long imputationId) {

        Task task = taskRepository.findByIdWithImputations(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Imputation imputation = imputationRepository.findById(imputationId)
                .orElseThrow(() -> new RuntimeException("Imputation not found"));

        // حذف
        task.getImputations().remove(imputation);
        imputation.setTask(null);

        recalculateConsumedHours(task);

        return TaskMapper.toDTO(taskRepository.save(task));
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
