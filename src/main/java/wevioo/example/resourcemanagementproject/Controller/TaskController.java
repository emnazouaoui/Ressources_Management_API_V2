package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Enums.Priority;
import wevioo.example.resourcemanagementproject.Enums.TaskStatus;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Service.TaskService;
import wevioo.example.resourcemanagementproject.Validator.Impl.TaskValidator;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "CRUD operations for tasks")
public class TaskController {

    private final TaskService taskService;


    @Operation(summary = "Create Task")
    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.create(dto));
    }

    @Operation(summary = "Update Task")
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id,
                                            @RequestBody TaskDTO dto) {

        return ResponseEntity.ok(taskService.update(id, dto));
    }

    @Operation(summary = "Get Task by ID")
    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return taskService.getById(id);
    }


    @Operation(summary = "Delete Task")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @Operation(summary = "Get all tasks with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(taskService.getAllTasks(page, pageSize, sortBy, sortDir));
    }

    @Operation(
            summary = "Recherche paginée des tâches",
            description = "Filtrer par titre, statut, priorité, projet, utilisateur, dates. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<TaskDTO>> searchTasks(

            @Parameter(description = "Filtrer par titre (recherche partielle)")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filtrer par description (recherche partielle)")
            @RequestParam(required = false) String description,

            @Parameter(description = "Filtrer par statut")
            @RequestParam(required = false) TaskStatus status,

            @Parameter(description = "Filtrer par priorité")
            @RequestParam(required = false) Priority priority,

            @Parameter(description = "Filtrer par ID de projet")
            @RequestParam(required = false) Long projectId,

            @Parameter(description = "Filtrer par nom de projet (recherche partielle)")
            @RequestParam(required = false) String projectName,

            @Parameter(description = "Filtrer par ID de l'utilisateur assigné")
            @RequestParam(required = false) Long assignedUserId,

            @Parameter(description = "Filtrer par username de l'utilisateur assigné")
            @RequestParam(required = false) String assignedUserUsername,

            @Parameter(description = "Date de début (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Date de fin (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-12-31T23:59:59")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Filtrer par heures estimées", example = "8.0")
            @RequestParam(required = false) Double estimatedHours,

            @Parameter(description = "Filtrer par heures consommées", example = "4.0")
            @RequestParam(required = false) Double consumedHours,

            @Parameter(description = "Numéro de page (commence à 1)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Champ de tri (name, email...)", example = "createdDate")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Direction du tri : ASC ou DESC", example = "ASC")
            @RequestParam(required = false) String sortDir

    ) {
        return ResponseEntity.ok(
                taskService.searchTasks(
                        title, description,
                        status, priority,
                        projectId, projectName,
                        assignedUserId, assignedUserUsername,
                        startDate, endDate,
                        estimatedHours, consumedHours,
                        page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
    }

    // ================= RELATIONS =================

    // ➕ ADD IMPUTATION
    @Operation(summary = "Assign imputation to task ")
    @PostMapping("/{taskId}/imputations/{imputationId}")
    public TaskDTO addImputation(
            @PathVariable Long taskId,
            @PathVariable Long imputationId) {

        return taskService.addImputationToTask(taskId, imputationId);
    }

    // ➖ REMOVE IMPUTATION
    @Operation(summary = "Remove imputation from task")
    @DeleteMapping("/{taskId}/imputations/{imputationId}")
    public TaskDTO removeImputation(
            @PathVariable Long taskId,
            @PathVariable Long imputationId) {

        return taskService.removeImputationFromTask(taskId, imputationId);
    }

}
