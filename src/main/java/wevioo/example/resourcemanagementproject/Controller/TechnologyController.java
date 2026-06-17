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
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Service.TechnologyService;
import wevioo.example.resourcemanagementproject.Validator.Impl.TechnologyValidator;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
@Tag(name = "Technology API", description = "CRUD operations for technologies")
public class TechnologyController {

    private final TechnologyService service;


    @PostMapping
    @Operation(summary = "Create technology")
    public ResponseEntity<TechnologyDTO> create(@RequestBody TechnologyDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update technology")
    public ResponseEntity<TechnologyDTO> update(@PathVariable Long id,
                                            @RequestBody TechnologyDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get technology by id")
    public TechnologyDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete technology")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(summary = "Get all technologies with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<TechnologyDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(service.getAll(page, pageSize, sortBy, sortDir));
    }

    @Operation(
            summary = "Recherche paginée des technologies",
            description = "Filtrer par nom . Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<TechnologyDTO>> searchTechnologies(

            @Parameter(description = "Filtrer par nom (recherche partielle)")
            @RequestParam(required = false) String name,

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
                service.searchTechnologies(
                        name, page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
    }


}
