package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "CRUD operations for users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create user")
    public UserDTO create(@Valid @RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public Page<UserDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return userService.getAll(page, size,sortBy);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by keyword")
    public List<UserDTO> search(@RequestParam String keyword) {
        return userService.search(keyword);
    }
}
