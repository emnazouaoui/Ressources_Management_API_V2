package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.List;

@RestController
@RequestMapping("/api/imputations")
@RequiredArgsConstructor
@Tag(name = "Imputations API", description = "CRUD operations for imputations")
public class ImputationController {


    private final ImputationService imputationService;

    @PostMapping
    @Operation(summary = "Create imputation")
    public ImputationDTO create(@RequestBody ImputationDTO dto) {
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
    public ImputationDTO update(@PathVariable Long id, @RequestBody ImputationDTO dto) {
        return imputationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an imputation")
    public void delete(@PathVariable Long id) {
        imputationService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search imputations by keyword ")
    public List<ImputationDTO> search(@RequestParam String keyword) {
        return imputationService.search(keyword);
    }


}
