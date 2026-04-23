package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import wevioo.example.resourcemanagementproject.DTO.TechnologyDTO;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;
import wevioo.example.resourcemanagementproject.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "CRUD operations for users")
public class UserController {

    private final UserService userService;
    private final UserHistoryRepository userHistoryRepository;


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

    // 🔥 assign technology
    @Operation(summary = "Assign technology to User")
    @PostMapping("/{id}/technologies/{techId}")
    public void assignTechnology(@PathVariable Long id,
                                 @PathVariable Long techId) {
        userService.assignTechnology(id, techId);
    }

    // 🔥 remove technology
    @Operation(summary = "Remove technology from user")
    @DeleteMapping("/{id}/technologies/{techId}")
    public void removeTechnology(@PathVariable Long id,
                                 @PathVariable Long techId) {
        userService.removeTechnology(id, techId);
    }

    // 🔥 get technologies of user
    @Operation(summary = "Get technologies of user")
    @GetMapping("/{id}/technologies")
    public List<Long> getUserTechnologies(@PathVariable Long id) {
        return userService.getUserTechnologies(id);
    }

//    // 🔥 search users by technology
//    @Operation(summary = "Search users by technology")
//    @GetMapping("/by-technology/{techId}")
//    public List<UserDTO> getUsersByTechnology(@PathVariable Long techId) {
//        return userService.getUsersByTechnology(techId);
//    }

    // 🔥 search users by technology name
    @Operation(summary = "Search users by technology")
    @GetMapping("/by-technology")
    public List<UserDTO> getUsersByTechnologyName(@RequestParam String name) {
        return userService.getUsersByTechnologyName(name);
    }

    @Operation(summary = "Get history user for test")
    @GetMapping("/{id}/history")
    public List<UserHistory> getHistory(@PathVariable Long id) {
        return userHistoryRepository.findAll()
                .stream()
                .filter(h -> h.getUser().getId() == id)
                .toList();
    }


}
