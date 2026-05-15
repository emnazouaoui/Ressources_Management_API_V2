package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.Enums.ClientType;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Service.ClientService;
import wevioo.example.resourcemanagementproject.Validator.Impl.ClientValidator;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client API", description = "CRUD operations for clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientValidator clientValidator;   // ← inject


    @Operation(summary = "Create new client")
    @PostMapping
    public ResponseEntity<ClientDTO> create(@RequestBody ClientDTO dto,
                                          BindingResult bindingResult) {
        // Lance la validation
        clientValidator.validate(dto, bindingResult);
        ValidationHelper.validate(bindingResult);   // ← lance ValidationException si errors

        return ResponseEntity.ok(clientService.create(dto));
    }

    @Operation(summary = "Update client")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable Long id,
                                          @RequestBody ClientDTO dto,
                                          BindingResult bindingResult) {
        //Lance la validation
        clientValidator.validate(dto, bindingResult);
        ValidationHelper.validate(bindingResult);

        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @Operation(summary = "Get client by id")
    @GetMapping("/{id}")
    public ClientDTO getById(@PathVariable Long id) {
        return clientService.getById(id);
    }


    @Operation(summary = "Get all clients with pagination")
    @GetMapping
    public ResponseEntity<PaginatedResponse<ClientDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(clientService.getAll(page, pageSize, sortBy, sortDir));
    }

    @Operation(summary = "Delete client")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }


    @Operation(
            summary = "Recherche paginée des clients",
            description = "Filtrer par un ou plusieurs attributs. Page commence à 1."
    )
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<ClientDTO>> searchClients(

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

            @Parameter(description = "Champ de tri (name, email...)", example = "createdDate")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Direction : ASC ou DESC", example = "ASC")
            @RequestParam(required = false) String sortDir

    ) {
        return ResponseEntity.ok(
                clientService.searchClients(
                        name, email, company, address, phone,
                        typeClient, page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
                )
        );
    }


}
