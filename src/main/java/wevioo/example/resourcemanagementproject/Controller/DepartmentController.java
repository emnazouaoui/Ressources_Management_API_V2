package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
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

    @GetMapping
    @Operation(summary = "Get all departments")
    public Page<DepartmentDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return service.getAll(page, size,sortBy);
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

    // 🔎 SEARCH
    @GetMapping("/search")
    @Operation(summary = "Search departments by keyword")
    public List<DepartmentDTO> search(@RequestParam String keyword) {
        return service.search(keyword);
    }


}
