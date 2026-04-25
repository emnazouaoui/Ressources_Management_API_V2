package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Service.ProjectTimeLineService;

import java.util.List;

@RestController
@RequestMapping("/api/project-timelines")
@RequiredArgsConstructor
@Tag(name = "Project Timeline", description = "Project Timeline APIs")
public class ProjectTimeLineController {


    private final ProjectTimeLineService service;

    @Operation(summary = "Create timeline")
    @PostMapping
    public ProjectTimeLineDTO create(@RequestBody ProjectTimeLineDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Update timeline")
    @PutMapping("/{id}")
    public ProjectTimeLineDTO update(
            @Parameter(description = "Timeline ID") @PathVariable Long id,
            @RequestBody ProjectTimeLineDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Get all timelines with pagination")
    @GetMapping
    public Page<ProjectTimeLineDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
    }


    @Operation(summary = "Get timelines by project")
    @GetMapping("/project/{projectId}")
    public List<ProjectTimeLineDTO> getByProject(@PathVariable Long projectId) {
        return service.getByProject(projectId);
    }

    @Operation(summary = "Search timelines")
    @GetMapping("/search")
    public List<ProjectTimeLineDTO> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    @Operation(summary = "Delete timeline")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
