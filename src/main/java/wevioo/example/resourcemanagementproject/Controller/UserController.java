package wevioo.example.resourcemanagementproject.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import wevioo.example.resourcemanagementproject.DTO.UserDTO;
import wevioo.example.resourcemanagementproject.Entity.UserHistory;
import wevioo.example.resourcemanagementproject.Enums.Level;
import wevioo.example.resourcemanagementproject.Exception.ValidationHelper;
import wevioo.example.resourcemanagementproject.Export.UserExportService;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Repository.UserHistoryRepository;
import wevioo.example.resourcemanagementproject.Service.UserService;
import wevioo.example.resourcemanagementproject.Validator.Impl.UserValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "CRUD operations for users")
public class UserController {

    private final UserService userService;
    private final UserHistoryRepository userHistoryRepository;
    private final UserValidator userValidator;   // ← inject
    private final UserExportService userExportService;


    @PostMapping
    @Operation(summary = "Create user")
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.create(dto));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,
                                          @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public ResponseEntity<PaginatedResponse<UserDTO>> getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir
    ) {
        return ResponseEntity.ok(userService.getAll(page, pageSize, sortBy, sortDir));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserDTO getById(@PathVariable Long id) {
        return userService.getById(id);
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
    public ResponseEntity<PaginatedResponse<UserDTO>> searchUsers(

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

            @Parameter(description = "Filtrer par téléphone")
            @RequestParam(required = false) String phone,

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

            @Parameter(description = "Numéro de page (commence à 1)", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Nombre de résultats par page", example = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,

            @Parameter(description = "Champ de tri (name, email...)", example = "createdDate")
            @RequestParam(required = false) String sortBy,

            @Parameter(description = "Direction du tri : ASC ou DESC", example = "ASC")
            @RequestParam(required = false) String sortDir

    ) {
        return ResponseEntity.ok(
                userService.searchUsers(
                        username, firstName, lastName, email,
                        active, level, phone,
                        roleId, roleName,
                        departmentId, departmentName,
                        managerId, managerUsername,
                        page, pageSize, sortBy, sortDir  // ← sortBy + sortDir مباشرة
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


    @Operation(summary = "Export users to Excel")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Level level,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) String managerUsername,
            @RequestParam(required = false) String phone
    ) throws IOException {

        byte[] excelFile = userExportService.exportUsers(
                username, firstName, lastName, email,
                active, level, roleId, roleName,
                departmentId, departmentName,
                managerId, managerUsername, phone
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=users_" + LocalDateTime.now() + ".xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
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
