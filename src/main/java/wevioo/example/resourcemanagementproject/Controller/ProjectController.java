package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects API", description = "CRUD operations for projects")
public class ProjectController {


    private final ProjectService service;

    // ================= CRUD =================

    @Operation(summary = "Create a new project")
    @PostMapping
    public ProjectDTO create(@RequestBody ProjectDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Update an existing project")
    @PutMapping("/{id}")
    public ProjectDTO update(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @RequestBody ProjectDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Get all projects with pagination")
    @GetMapping
    public Page<ProjectDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {

        return service.getAll(page, size,sortBy);
    }



    @Operation(summary = "Search projects by keyword ")
    @GetMapping("/search")
    public List<ProjectDTO> search(
            @Parameter(description = "Keyword for search") @RequestParam String keyword) {
        return service.search(keyword);
    }

    @Operation(summary = "Delete a project")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        service.delete(id);
    }

    // ================= RELATIONS =================

    @Operation(summary = "Assign technology to project")
    @PostMapping("/{id}/technologies/{techId}")
    public void addTech(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Parameter(description = "Technology ID") @PathVariable Long techId) {
        service.assignTechnologies(id, List.of(techId));
    }

    @Operation(summary = "Remove technology from project")
    @DeleteMapping("/{id}/technologies/{techId}")
    public void removeTech(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Parameter(description = "Technology ID") @PathVariable Long techId) {
        service.removeTechnology(id, techId);
    }

    @Operation(summary = "Assign user to project")
    @PostMapping("/{id}/users/{userId}")
    public void addUser(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Parameter(description = "User ID") @PathVariable Long userId) {
        service.assignUsers(id, List.of(userId));
    }

    @Operation(summary = "Remove user from project")
    @DeleteMapping("/{id}/users/{userId}")
    public void removeUser(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Parameter(description = "User ID") @PathVariable Long userId) {
        service.removeUser(id, userId);
    }

    // 🔥 ADD Timeline to project
    @Operation(summary = "ADD Timeline to project")
    @PostMapping("/{projectId}/timelines/{timelineId}")
    public void addTimeline(@PathVariable Long projectId,
                            @PathVariable Long timelineId) {
        service.addTimeline(projectId, timelineId);
    }

    // 🔥 REMOVE Timeline from project
    @Operation(summary = "REMOVE Timeline from project")
    @DeleteMapping("/{projectId}/timelines/{timelineId}")
    public void removeTimeline(@PathVariable Long projectId,
                               @PathVariable Long timelineId) {
        service.removeTimeline(projectId, timelineId);
    }


}
