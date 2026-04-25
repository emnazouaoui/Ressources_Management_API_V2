package wevioo.example.resourcemanagementproject.Mapper;

import org.springframework.stereotype.Component;
import wevioo.example.resourcemanagementproject.DTO.TaskDTO;
import wevioo.example.resourcemanagementproject.Entity.Task;

@Component
public class TaskMapper {
    // ENTITY → DTO
    public static TaskDTO toDTO(Task task) {

        if (task == null) return null;

        TaskDTO dto = new TaskDTO();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setConsumedHours(task.getConsumedHours());

        if (task.getProject() != null)
            dto.setProjectId(task.getProject().getId());

        if (task.getAssignedUser() != null)
            dto.setAssignedUserId(task.getAssignedUser().getId());

        return dto;
    }

    // DTO → ENTITY (basic)
    public static Task toEntity(TaskDTO dto) {

        if (dto == null) return null;

        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setEstimatedHours(dto.getEstimatedHours());
        task.setConsumedHours(dto.getConsumedHours());

        return task;
    }
}
