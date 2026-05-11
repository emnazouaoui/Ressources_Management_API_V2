package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
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
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        CustomSort sort = buildSort(sortBy, sortDir);
        return service.getAll(page, pageSize,sort);
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

            @Parameter(description = "Numéro de page (commence à 1)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Champ de tri (name, email...)", example = "name")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Direction du tri : ASC ou DESC", example = "ASC")
            @RequestParam(required = false) String sortDir
    ) {
        CustomSort sort = buildSort(sortBy, sortDir);

        return ResponseEntity.ok(
                service.searchRoles(
                        name, description, page, pageSize, sort
                )
        );
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
