package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return clientService.getAll(page, size,sortBy);
    }

    @Operation(summary = "Delete client")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }

    @Operation(summary = "Search clients with keyword")
    @GetMapping("/search")
    public List<ClientDTO> search(@RequestParam String keyword) {
        return clientService.search(keyword);
    }
}
