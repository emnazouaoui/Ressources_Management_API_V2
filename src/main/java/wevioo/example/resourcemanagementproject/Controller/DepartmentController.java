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
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
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

//    @GetMapping
//    @Operation(summary = "Get all departments")
//    public Page<DepartmentDTO> getAll(
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "5") Integer pageSize,
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(required = false) String sortDir
//    ) {
//        CustomSort sort = buildSort(sortBy, sortDir);
//        return service.getAll(page, pageSize,sort);
//    }
    @Operation(summary = "Get all departments with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<DepartmentDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(service.getAll(page, pageSize, sortBy, sortDir));
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


//    @Operation(
//            summary = "Recherche paginée des départements",
//            description = "Filtrer par nom et/ou description. Tous les champs sont optionnels."
//    )
//    @GetMapping("/search")
//    public ResponseEntity<Page<DepartmentDTO>> searchDepartments(
//
//            @Parameter(description = "Filtrer par nom (recherche partielle)")
//            @RequestParam(required = false) String name,
//
//            @Parameter(description = "Filtrer par description (recherche partielle)")
//            @RequestParam(required = false) String description,
//
//            @Parameter(description = "Numéro de page (commence à 1)", example = "1")
//            @RequestParam(defaultValue = "1") Integer page,
//
//            @Parameter(description = "Nombre de résultats par page", example = "10")
//            @RequestParam(defaultValue = "10") Integer pageSize,
//
//            @Parameter(description = "Champ de tri (name, email...)", example = "name")
//            @RequestParam(required = false) String sortBy,
//
//            @Parameter(description = "Direction du tri : ASC ou DESC", example = "ASC")
//            @RequestParam(required = false) String sortDir
//    ) {
//        CustomSort sort = buildSort(sortBy, sortDir);
//        return ResponseEntity.ok(
//                service.searchDepartments(
//                        name, description, page, pageSize, sort
//                )
//        );
//    }

    @Operation(
            summary = "Recherche paginée des départements",
            description = "Filtrer par nom et/ou description. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<DepartmentDTO>> searchDepartments(

            @Parameter(description = "Filtrer par nom")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filtrer par description (recherche partielle)")
            @RequestParam(required = false) String description,

            @Parameter(description = "Numéro de page (commence à 1)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Champ de tri (name, email...)", example = "createdDate")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Direction : ASC ou DESC", example = "ASC")
            @RequestParam(required = false) String sortDir

    ) {
        return ResponseEntity.ok(
                service.searchDepartments(
                        name, description, page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
    }


//    // -------------------------------------------------------------------------
//    // Helper — construit CustomSort uniquement si les deux params sont fournis
//    // -------------------------------------------------------------------------
//    private CustomSort buildSort(String sortBy, String sortDir) {
//        if (sortBy == null || sortDir == null) return null;
//        CustomSort sort = new CustomSort();
//        sort.setColumnKey(sortBy);
//        sort.setOrder(Sort.Direction.fromString(sortDir));
//        return sort;
//    }


}
