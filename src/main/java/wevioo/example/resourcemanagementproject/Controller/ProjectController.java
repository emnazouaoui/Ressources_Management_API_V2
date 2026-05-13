package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Service.ProjectService;

import java.time.LocalDateTime;
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
    public ProjectDTO create(@Valid @RequestBody ProjectDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Update an existing project")
    @PutMapping("/{id}")
    public ProjectDTO update(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Valid @RequestBody ProjectDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Get all projects with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ProjectDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(service.getAll(page, pageSize, sortBy, sortDir));
    }


    @Operation(
            summary = "Recherche paginée des projets",
            description = "Filtrer par nom, statut, manager, client, dates, progression. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<ProjectDTO>> searchProjects(

            @Parameter(description = "Filtrer par nom (recherche partielle)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filtrer par description (recherche partielle)")
            @RequestParam(required = false) String description,

            @Parameter(description = "Filtrer par statut du projet")
            @RequestParam(required = false) ProjectStatus status,

            @Parameter(description = "Filtrer par ID du project manager")
            @RequestParam(required = false) Long projectManagerId,

            @Parameter(description = "Filtrer par username du project manager")
            @RequestParam(required = false) String projectManagerUsername,

            @Parameter(description = "Filtrer par ID du client")
            @RequestParam(required = false) Long clientId,

            @Parameter(description = "Filtrer par nom du client (recherche partielle)")
            @RequestParam(required = false) String clientName,

            @Parameter(description = "Date de début (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Date de fin (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-12-31T23:59:59")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Progression (%)", example = "0.0")
            @RequestParam(required = false) Double progressPercent,

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
                service.searchProjects(
                        name, description, status,
                        projectManagerId, projectManagerUsername,
                        clientId, clientName,
                        startDate, endDate,
                        progressPercent,
                        page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
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


    // 🔥 ASSIGN TASK TO PROJECT
    @Operation(summary = "Assign task to project")
    @PostMapping("/{projectId}/tasks/{taskId}")
    public void assignTask(@PathVariable Long projectId,
                           @PathVariable Long taskId) {

        service.assignTask(projectId, taskId);
    }

    // 🔥 REMOVE TASK FROM PROJECT
    @Operation(summary = "REMOVE task from project")
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public void removeTask(@PathVariable Long projectId,
                           @PathVariable Long taskId) {

        service.removeTask(projectId, taskId);
    }

    // -------------------------------------------------------------------------
    // Helper — construit CustomSort uniquement si les deux params sont fournis
    // -------------------------------------------------------------------------
    private CustomSort buildSort(String sortBy, String sortDir) {
        if (sortBy == null || sortDir == null) return null;
        CustomSort sort = new CustomSort();
        sort.setColumnKey(sortBy);
        sort.setOrder(Sort.Direction.fromString(sortDir));
        return sort;
    }



}
