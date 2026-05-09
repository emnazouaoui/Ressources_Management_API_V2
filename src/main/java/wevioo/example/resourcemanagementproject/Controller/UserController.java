package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Enums.Level;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;
import wevioo.example.resourcemanagementproject.Service.UserService;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Operation(
            summary = "Recherche paginée des utilisateurs",
            description = "Filtrer par username, nom, email, rôle, département, manager. Tous les champs sont optionnels."
    )
    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers(

            @Parameter(description = "Filtrer par username")
            @RequestParam(required = false) String username,

            @Parameter(description = "Filtrer par prénom")
            @RequestParam(required = false) String firstName,

            @Parameter(description = "Filtrer par nom")
            @RequestParam(required = false) String lastName,

            @Parameter(description = "Filtrer par email")
            @RequestParam(required = false) String email,

            @Parameter(description = "Filtrer par statut actif : true ou false")
            @RequestParam(required = false) Boolean active,

            @Parameter(description = "Filtrer par niveau (JUNIOR, MID, SENIOR…)")
            @RequestParam(required = false) Level level,

            @Parameter(description = "Filtrer par ID du rôle")
            @RequestParam(required = false) Long roleId,

            @Parameter(description = "Filtrer par nom du rôle")
            @RequestParam(required = false) String roleName,

            @Parameter(description = "Filtrer par ID du département")
            @RequestParam(required = false) Long departmentId,

            @Parameter(description = "Filtrer par nom du département")
            @RequestParam(required = false) String departmentName,

            @Parameter(description = "Filtrer par ID du manager")
            @RequestParam(required = false) Long managerId,

            @Parameter(description = "Filtrer par username du manager")
            @RequestParam(required = false) String managerUsername,

            @Parameter(description = "Numéro de page (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Champ de tri (username, firstName, lastName, createdDate…)", example = "username")
            @RequestParam(defaultValue = "username") String sortBy,

            @Parameter(description = "Direction du tri : asc ou desc", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir

    ) {
        return ResponseEntity.ok(
                userService.searchUsers(
                        username, firstName, lastName, email,
                        active, level,
                        roleId, roleName,
                        departmentId, departmentName,
                        managerId, managerUsername,
                        page, size, sortBy, sortDir
                )
        );
    }

    @Operation(summary = "Get history user for test")
    @GetMapping("/{id}/history")
    public List<UserHistory> getHistory(@PathVariable Long id) {
        return userHistoryRepository.findAll()
                .stream()
                .filter(h -> h.getUser().getId() == id)
                .toList();
    }

//    //------------------------- Upload photo user-----------------------------//
//
//    @Operation(summary = "Upload photo user")
//    @PostMapping(value ="/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public UserDTO uploadPhoto(@PathVariable Long id,
//                               @RequestParam MultipartFile file) {
//        return userService.uploadUserPhoto(id, file);
//    }
//
//    @GetMapping("/{id}/photo")
//    public ResponseEntity<Resource> getUserPhoto(@PathVariable Long id) {
//
//        Resource resource = userService.getUserPhoto(id);
//
//        try {
//            Path filePath = Paths.get("uploads")
//                    .resolve(resource.getFilename())
//                    .normalize();
//
//            String contentType = Files.probeContentType(filePath);
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "inline; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } catch (Exception e) {
//            throw new RuntimeException("Error returning image");
//        }
//    }


}
