package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.LeaveRequestDTO;
import wevioo.example.resourcemanagementproject.Service.LeaveRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave requests API", description = "CRUD operations for Leave requests")
public class LeaveRequestController {

    private final LeaveRequestService service;

    @PostMapping
    @Operation(summary = "Create a new leave request")
    public LeaveRequestDTO create(@RequestBody LeaveRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing leave request")
    public LeaveRequestDTO update(@PathVariable Long id,
                                  @RequestBody LeaveRequestDTO dto) {
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

    // 🔥 SEARCH API
    @GetMapping("/search")
    @Operation(summary = "Search projects by keyword ")
    public List<LeaveRequestDTO> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}
