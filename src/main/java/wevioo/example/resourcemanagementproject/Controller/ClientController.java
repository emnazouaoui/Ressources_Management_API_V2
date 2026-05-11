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
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client API", description = "CRUD operations for clients")
public class ClientController {

    private final ClientService clientService;

//    public ClientController(ClientService service) {
//        this.clientService = service;
//    }

    @Operation(summary = "Create new client")
    @PostMapping
    public ClientDTO create(@Valid @RequestBody ClientDTO dto) {
        return clientService.create(dto);
    }

    @Operation(summary = "Update client")
    @PutMapping("/{id}")
    public ClientDTO update(@Valid @PathVariable Long id, @RequestBody ClientDTO dto) {
        return clientService.update(id, dto);
    }

    @Operation(summary = "Get client by id")
    @GetMapping("/{id}")
    public ClientDTO getById(@PathVariable Long id) {
        return clientService.getById(id);
    }

    @Operation(summary = "Get all clients with pagination")
    @GetMapping
    public Page<ClientDTO> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        CustomSort sort = buildSort(sortBy, sortDir);
        return clientService.getAll(page, pageSize, sort);
    }

    @Operation(summary = "Delete client")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }

    @Operation(
            summary = "Recherche paginée des clients",
            description = "Filtrer par un ou plusieurs attributs. Tous les champs sont optionnels et combinables."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ClientDTO>> searchClients(

            @Parameter(description = "Filtrer par nom")
            @RequestParam(required = false) String name,

            @Parameter(description = "Filtrer par email")
            @RequestParam(required = false) String email,

            @Parameter(description = "Filtrer par société")
            @RequestParam(required = false) String company,

            @Parameter(description = "Filtrer par adresse")
            @RequestParam(required = false) String address,

            @Parameter(description = "Filtrer par téléphone")
            @RequestParam(required = false) String phone,

            @Parameter(description = "INTERNAL ou EXTERNAL")
            @RequestParam(required = false) ClientType typeClient,

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
                clientService.searchClients(
                        name, email, company, address, phone,
                        typeClient,  page, pageSize, sort
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
