package wevioo.example.resourcemanagementproject.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "CRUD operations for tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Create Task")
    @PostMapping
    public TaskDTO create(@RequestBody TaskDTO dto) {
        return taskService.create(dto);
    }

    @Operation(summary = "Get Task by ID")
    @GetMapping("/{id}")
    public TaskDTO getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Update Task")
    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskDTO dto) {
        return taskService.update(id, dto);
    }

    @Operation(summary = "Delete Task")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @Operation(summary = "Get all tasks with pagination")
    @GetMapping
    public Page<TaskDTO> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return taskService.getAllTasks(page, size);
    }

    @Operation(summary = "Search tasks by keyword ")
    @GetMapping("/search")
    public List<TaskDTO> search(
            @Parameter(description = "Keyword for search") @RequestParam String keyword) {
        return taskService.search(keyword);
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
