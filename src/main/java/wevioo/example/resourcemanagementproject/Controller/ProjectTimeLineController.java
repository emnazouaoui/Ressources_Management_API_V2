package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Service.ProjectTimeLineService;
import wevioo.example.resourcemanagementproject.Validator.Impl.ProjectTimeLineValidator;

import java.util.List;

@RestController
@RequestMapping("/api/project-timelines")
@RequiredArgsConstructor
@Tag(name = "Project Timeline", description = "Project Timeline APIs")
public class ProjectTimeLineController {


    private final ProjectTimeLineService service;
    private final ProjectTimeLineValidator projectTimeLineValidator;  // ← inject


    @Operation(summary = "Create timeline")
    @PostMapping
    public ResponseEntity<ProjectTimeLineDTO> create(@RequestBody ProjectTimeLineDTO dto,
                                            BindingResult bindingResult) {
        // Lance la validation
        projectTimeLineValidator.validate(dto, bindingResult);
        ValidationHelper.validate(bindingResult);

        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Update timeline")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectTimeLineDTO> update(@PathVariable Long id,
                                            @RequestBody ProjectTimeLineDTO dto,
                                            BindingResult bindingResult) {
        //Lance la validation
        projectTimeLineValidator.validate(dto, bindingResult);
        ValidationHelper.validate(bindingResult);

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Get all timelines with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ProjectTimeLineDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(service.getAll(page, pageSize, sortBy, sortDir));
    }


    @Operation(summary = "Get timelines by project")
    @GetMapping("/project/{projectId}")
    public List<ProjectTimeLineDTO> getByProject(@PathVariable Long projectId) {
        return service.getByProject(projectId);
    }

    @Operation(
            summary = "Recherche paginée des project timelines",
            description = "Filtrer par titre, description, version, type, projet. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<ProjectTimeLineDTO>> searchProjectTimeLines(

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
                service.searchProjectTimeLines(
                        title, description, version,
                        type, deliveredToClient,
                        projectId, name,progressPercent,
                        page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
    }

    @Operation(summary = "Delete timeline")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
