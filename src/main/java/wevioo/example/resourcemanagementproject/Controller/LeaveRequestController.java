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
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestStatus;
import wevioo.example.resourcemanagementproject.Enums.LeaveRequestType;
import wevioo.example.resourcemanagementproject.Service.LeaveRequestService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave requests API", description = "CRUD operations for Leave requests")
public class LeaveRequestController {

    private final LeaveRequestService service;

    @PostMapping
    @Operation(summary = "Create a new leave request")
    public LeaveRequestDTO create(@Valid @RequestBody LeaveRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing leave request")
    public LeaveRequestDTO update(@PathVariable Long id,
                                  @Valid @RequestBody LeaveRequestDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping
    @Operation(summary = "Get all leave request with pagination")
    public Page<LeaveRequestDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a leave request with Id")
    public LeaveRequestDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a leave request")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    @Operation(
            summary = "Recherche paginée des demandes de congé",
            description = "Filtrer par type, statut, utilisateur, manager, dates. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<LeaveRequestDTO>> searchLeaveRequests(

            @Parameter(description = "Filtrer par raison (recherche partielle)")
            @RequestParam(required = false) String reason,

            @Parameter(description = "Type de congé (ex: ANNUAL, SICK...)")
            @RequestParam(required = false) LeaveRequestType type,

            @Parameter(description = "Statut (PENDING, APPROVED, REJECTED)")
            @RequestParam(required = false) LeaveRequestStatus status,

            @Parameter(description = "Filtrer par ID utilisateur")
            @RequestParam(required = false) Long userId,

            @Parameter(description = "Filtrer par ID project manager")
            @RequestParam(required = false) Long projectManagerId,

            @Parameter(description = "Filtrer par nom utilisateur")
            @RequestParam(required = false) String username,

            @Parameter(description = "Date de début (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Date de fin (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-12-31T23:59:59")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,

            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Champ de tri (startDate, endDate, status…)", example = "startDate")
            @RequestParam(defaultValue = "startDate") String sortBy,

            @Parameter(description = "Direction du tri : asc ou desc", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir

    ) {
        return ResponseEntity.ok(
                service.searchLeaveRequests(
                        reason, type, status,
                        userId, projectManagerId,
                        username,
                        startDate, endDate,
                        page, size, sortBy, sortDir
                )
        );
    }

    //----------------------- Approve/Reject leave request -------------------------//

    @PutMapping("/{id}/status")
    @Operation(summary = "Update status for leave request ")
    public LeaveRequestDTO updateStatus(
            @PathVariable Long id,
            @Valid @RequestParam LeaveRequestStatus status
    ) {
        return service.updateStatus(id, status);
    }


}
