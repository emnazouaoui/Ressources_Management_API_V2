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
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Service.TechnologyService;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
@Tag(name = "Technology API", description = "CRUD operations for technologies")
public class TechnologyController {

    private final TechnologyService service;

    @PostMapping
    @Operation(summary = "Create technology")
    public TechnologyDTO create(@Valid @RequestBody TechnologyDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get technology by id")
    public TechnologyDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update technology")
    public TechnologyDTO update(@Valid @PathVariable Long id, @RequestBody TechnologyDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete technology")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // 📄 PAGINATION
    @GetMapping
    @Operation(summary = "Get all technologies with pagination")
    public Page<TechnologyDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
    }

    @Operation(
            summary = "Recherche paginée des technologies",
            description = "Filtrer par nom . Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<TechnologyDTO>> searchTechnologies(

            @Parameter(description = "Filtrer par nom (recherche partielle)")
            @RequestParam(required = false) String name,

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
                service.searchTechnologies(
                        name, page, size, sortBy, sortDir
                )
        );
    }


}
