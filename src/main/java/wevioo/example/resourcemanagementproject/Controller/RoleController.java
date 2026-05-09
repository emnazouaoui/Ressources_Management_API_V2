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
import wevioo.example.resourcemanagementproject.DTO.RoleDTO;
import wevioo.example.resourcemanagementproject.Service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role API", description = "CRUD operations for roles")
public class RoleController {

    private final RoleService service;

    @PostMapping
    @Operation(summary = "Create role")
    public RoleDTO create(@Valid @RequestBody RoleDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by id")
    public RoleDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update role")
    public RoleDTO update(@Valid @PathVariable Long id, @RequestBody RoleDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete role")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 📄 PAGINATION
    @GetMapping
    @Operation(summary = "Get all roles with pagination")
    public Page<RoleDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
    }


    @Operation(
            summary = "Recherche paginée des roles",
            description = "Filtrer par nom et/ou description. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<RoleDTO>> searchRoles(

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
                service.searchRoles(
                        name, description, page, size, sortBy, sortDir
                )
        );
    }
}
