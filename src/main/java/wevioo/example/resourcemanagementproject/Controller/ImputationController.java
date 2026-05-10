package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import wevioo.example.resourcemanagementproject.DTO.ImputationDTO;
import wevioo.example.resourcemanagementproject.Service.ImputationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/imputations")
@RequiredArgsConstructor
@Tag(name = "Imputations API", description = "CRUD operations for imputations")
public class ImputationController {


    private final ImputationService imputationService;

    @PostMapping
    @Operation(summary = "Create imputation")
    public ImputationDTO create(@Valid @RequestBody ImputationDTO dto) {
        return imputationService.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an imputation with Id")
    public ImputationDTO getById(@PathVariable Long id) {
        return imputationService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all imputations")
    public Page<ImputationDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return imputationService.getAll(page, size,sortBy);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing imputation")
    public ImputationDTO update(@PathVariable Long id, @Valid @RequestBody ImputationDTO dto) {
        return imputationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an imputation")
    public void delete(@PathVariable Long id) {
        imputationService.delete(id);
    }

    @Operation(
            summary = "Recherche paginée des imputations",
            description = "Filtrer par commentaire, tâche, utilisateur, plage de dates et heures. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ImputationDTO>> searchImputations(

            @Parameter(description = "Filtrer par commentaire (recherche partielle)")
            @RequestParam(required = false) String comment,

            @Parameter(description = "Filtrer par ID de tâche")
            @RequestParam(required = false) Long taskId,

            @Parameter(description = "Filtrer par ID d'utilisateur")
            @RequestParam(required = false) Long userId,

            @Parameter(description = "Filtrer par titre de tâche (recherche partielle)")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filtrer par nom d'utilisateur (recherche partielle)")
            @RequestParam(required = false) String username,

            @Parameter(description = "Date (format: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,

            @Parameter(description = "Heures ", example = "1.0")
            @RequestParam(required = false) Double hours,

            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Champ de tri (date, hours, createdDate…)", example = "date")
            @RequestParam(defaultValue = "date") String sortBy,

            @Parameter(description = "Direction du tri : asc ou desc", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir

    ) {
        return ResponseEntity.ok(
                imputationService.searchImputations(
                        comment, title, username,
                        taskId, userId,
                        date, hours,
                        page, size, sortBy, sortDir
                )
        );
    }


}
