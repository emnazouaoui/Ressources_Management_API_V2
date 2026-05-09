package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
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

//    @Operation(summary = "Search timelines")
//    @GetMapping("/search")
//    public List<ProjectTimeLineDTO> search(@RequestParam String keyword) {
//        return service.search(keyword);
//    }

    @Operation(
            summary = "Recherche paginée des project timelines",
            description = "Filtrer par titre, description, version, type, projet. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectTimeLineDTO>> searchProjectTimeLines(

            @Parameter(description = "Filtrer par titre (recherche partielle)")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filtrer par description (recherche partielle)")
            @RequestParam(required = false) String description,

            @Parameter(description = "Filtrer par version (recherche partielle)")
            @RequestParam(required = false) String version,

            @Parameter(description = "Filtrer par type de timeline")
            @RequestParam(required = false) ProjectTimeLineType type,

            @Parameter(description = "Filtrer par livraison client : true ou false")
            @RequestParam(required = false) Boolean deliveredToClient,

            @Parameter(description = "Filtrer par ID de projet")
            @RequestParam(required = false) Long projectId,

            @Parameter(description = "Filtrer par nom de projet (recherche partielle)")
            @RequestParam(required = false) String name,

            @Parameter(description = "progressPercent ", example = "1.0")
            @RequestParam(required = false) Double progressPercent,

            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Champ de tri (title, version, createdDate…)", example = "createdDate")
            @RequestParam(defaultValue = "createdDate") String sortBy,

            @Parameter(description = "Direction du tri : asc ou desc", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir

    ) {
        return ResponseEntity.ok(
                service.searchProjectTimeLines(
                        title, description, version,
                        type, deliveredToClient,
                        projectId, name,progressPercent,
                        page, size, sortBy, sortDir
                )
        );
    }

    @Operation(summary = "Delete timeline")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
