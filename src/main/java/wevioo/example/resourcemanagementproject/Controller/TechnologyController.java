package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
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
    public TechnologyDTO create(@RequestBody TechnologyDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get technology by id")
    public TechnologyDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update technology")
    public TechnologyDTO update(@PathVariable Long id, @RequestBody TechnologyDTO dto) {
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


    // 🔎 SEARCH
    @GetMapping("/search")
    @Operation(summary = "Search technologies by keyword")
    public List<TechnologyDTO> search(@RequestParam String keyword) {
        return service.search(keyword);
    }


}
