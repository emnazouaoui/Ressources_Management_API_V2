package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department API", description = "CRUD operations for departments")
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    @Operation(summary = "Create department")
    public DepartmentDTO create(@Valid @RequestBody DepartmentDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public Page<DepartmentDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by id")
    public DepartmentDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public DepartmentDTO update(@Valid @PathVariable Long id, @RequestBody DepartmentDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @Operation(
            summary = "Recherche paginée des départements",
            description = "Filtrer par nom et/ou description. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentDTO>> searchDepartments(

            @Parameter(description = "Filtrer par nom (recherche partielle)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filtrer par description (recherche partielle)")
            @RequestParam(required = false) String description,

            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Champ de tri (name, createdDate…)", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(description = "Direction du tri : asc ou desc", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                service.searchDepartments(
                        name, description, page, size, sortBy, sortDir
                )
        );
    }


}
