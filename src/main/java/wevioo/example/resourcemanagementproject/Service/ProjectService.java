package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Entity.*;
import wevioo.example.resourcemanagementproject.Enums.ProjectField;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Mapper.ProjectMapper;
import wevioo.example.resourcemanagementproject.Repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {


    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final TechnologyRepository technologyRepository;

    private final UserProjectRepository userProjectRepository;
    private final ProjectTimeLineRepository projectTimeLineRepository;
    private final TaskRepository taskRepository;

    private final ProjectMapper mapper;

    private final ProjectHistoryService projectHistoryService;


    // 🔥 CREATE
    public ProjectDTO create(ProjectDTO dto) {

        Project p = new Project();
        mapper.updateEntity(dto, p);

        p.setStatus(ProjectStatus.valueOf(dto.getStatus()));

        p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found")));

        p.setClient(clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found")));

        Project saved = projectRepository.save(p);

        // assign relations
        assignUsers(saved.getId(), dto.getUserIds());
        assignTechnologies(saved.getId(), dto.getTechnologyIds());

        return mapper.toDTO(saved);
    }

//    // 🔥 UPDATE
//    public ProjectDTO update(Long id, ProjectDTO dto) {
//
//        Project p = projectRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Project not found"));
//
//        mapper.updateEntity(dto, p);
//
//        p.setStatus(ProjectStatus.valueOf(dto.getStatus()));
//
//        p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
//                .orElseThrow(() -> new RuntimeException("Manager not found")));
//
//        p.setClient(clientRepository.findById(dto.getClientId())
//                .orElseThrow(() -> new RuntimeException("Client not found")));
//
//        Project saved = projectRepository.save(p);
//
//        return mapper.toDTO(saved);
//    }
public ProjectDTO update(Long id, ProjectDTO dto) {

    Project p = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    // =========================
    // 🔥 HISTORY (OLD VALUES)
    // =========================
    String oldName = p.getName();
    String oldDescription = p.getDescription();
    String oldStatus = p.getStatus() != null ? p.getStatus().name() : null;
    String oldStartDate = String.valueOf(p.getStartDate());
    String oldEndDate = String.valueOf(p.getEndDate());
    String oldProgress = String.valueOf(p.getProgressPercent());

    String oldManager = p.getProjectManager() != null
            ? String.valueOf(p.getProjectManager().getId())
            : null;

    String oldClient = p.getClient() != null
            ? String.valueOf(p.getClient().getId())
            : null;

    // =========================
    // 🔥 UPDATE FIELDS
    // =========================
    mapper.updateEntity(dto, p);

    if (dto.getStatus() != null) {
        p.setStatus(ProjectStatus.valueOf(dto.getStatus()));
    }

    p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
            .orElseThrow(() -> new RuntimeException("Manager not found")));

    p.setClient(clientRepository.findById(dto.getClientId())
            .orElseThrow(() -> new RuntimeException("Client not found")));

    Project saved = projectRepository.save(p);

    // =========================
    // 🔥 HISTORY (NEW VALUES)
    // =========================
    projectHistoryService.saveHistory(p, ProjectField.NAME,
            oldName, saved.getName());

    projectHistoryService.saveHistory(p, ProjectField.DESCRIPTION,
            oldDescription, saved.getDescription());

    projectHistoryService.saveHistory(p, ProjectField.STATUS,
            oldStatus, saved.getStatus().name());

    projectHistoryService.saveHistory(p, ProjectField.START_DATE,
            oldStartDate, String.valueOf(saved.getStartDate()));

    projectHistoryService.saveHistory(p, ProjectField.END_DATE,
            oldEndDate, String.valueOf(saved.getEndDate()));

    projectHistoryService.saveHistory(p, ProjectField.PROGRESS,
            oldProgress, String.valueOf(saved.getProgressPercent()));

    projectHistoryService.saveHistory(p, ProjectField.PROJECT_MANAGER,
            oldManager,
            dto.getProjectManagerId() != null ? dto.getProjectManagerId().toString() : null);

    projectHistoryService.saveHistory(p, ProjectField.CLIENT,
            oldClient,
            dto.getClientId() != null ? dto.getClientId().toString() : null);

    return mapper.toDTO(saved);
}


//    // 🔥 GET ALL
//    public List<ProjectDTO> getAll() {
//
//        return projectRepository.findAll()
//                .stream()
//                .map(mapper::toDTO)
//                .toList();
//    }
    //Get All with pagination
    public Page<ProjectDTO> getAll(int page, int size,String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return projectRepository.findAll(pageable)
                .map(mapper::toDTO);
    }

    //  SEARCH
    public Page<ProjectDTO> searchProjects(
            String name,
            String description,
            ProjectStatus status,
            Long projectManagerId,
            String projectManagerUsername,
            Long clientId,
            String clientName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Double progressPercent,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return projectRepository.searchProjects(
                normalize(name),
                normalize(description),
                status,
                projectManagerId,
                normalize(projectManagerUsername),
                clientId,
                normalize(clientName),
                startDate,
                endDate,
                progressPercent,
                pageable
        ).map(mapper::toDTO);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    // 🔥 DELETE
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    // =========================
    // 🔥 RELATIONS
    // =========================

//    public void assignTechnologies(Long projectId, List<Long> techIds) {
//        if (techIds == null) return;
//
//        for (Long techId : techIds) {
//
//            if (projectTechnologyRepository.existsByProjectIdAndTechnologyId(projectId, techId))
//                continue;
//
//            ProjectTechnology pt = new ProjectTechnology();
//
//            Project p = new Project();
//            p.setId(projectId);
//
//            Technology t = technologyRepository.findById(techId)
//                    .orElseThrow(() -> new RuntimeException("Tech not found"));
//
//            pt.setProject(p);
//            pt.setTechnology(t);
//
//            projectTechnologyRepository.save(pt);
//        }
//    }

    // ✅ Après
    public void assignTechnologies(Long projectId, List<Long> techIds) {
        if (techIds == null) return;
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Technology> techs = technologyRepository.findAllById(techIds);
        project.getTechnologies().addAll(
                techs.stream()
                        .filter(t -> !project.getTechnologies().contains(t))
                        .toList()
        );
        projectRepository.save(project);
    }

    public void assignUsers(Long projectId, List<Long> userIds) {
        if (userIds == null) return;

        for (Long userId : userIds) {

            if (userProjectRepository.existsByProjectIdAndUserId(projectId, userId))
                continue;

            UserProject up = new UserProject();

            Project p = new Project();
            p.setId(projectId);

            User u = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            up.setProject(p);
            up.setUser(u);

            userProjectRepository.save(up);
        }
    }

    public void addTimeline(Long projectId, Long timelineId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectTimeLine timeline = projectTimeLineRepository.findById(timelineId)
                .orElseThrow(() -> new RuntimeException("Timeline not found"));

        // check already assigned
        if (timeline.getProject() != null &&
                timeline.getProject().getId() == projectId) {
            throw new RuntimeException("Timeline already assigned to this project");
        }

        timeline.setProject(project);

        projectTimeLineRepository.save(timeline);
    }

    // 🔥 ASSIGN ONE TASK
    public void assignTask(Long projectId, Long taskId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setProject(project);
        taskRepository.save(task);
    }



    // remove
//    public void removeTechnology(Long projectId, Long techId) {
//        projectTechnologyRepository.deleteByProjectIdAndTechnologyId(projectId, techId);
//    }

    public void removeTechnology(Long projectId, Long techId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.getTechnologies().removeIf(t -> t.getId() == techId);
        projectRepository.save(project);
    }

    public void removeUser(Long projectId, Long userId) {
        userProjectRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    public void removeTimeline(Long projectId, Long timelineId) {

        ProjectTimeLine timeline = projectTimeLineRepository.findById(timelineId)
                .orElseThrow(() -> new RuntimeException("Timeline not found"));

        if (timeline.getProject() == null ||
                timeline.getProject().getId() != projectId) {
            throw new RuntimeException("Timeline not linked to this project");
        }

        projectTimeLineRepository.delete(timeline);
    }

    // 🔥 REMOVE TASK (with validation)
    public void removeTask(Long projectId, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // ✅ check task belongs to project
        if (task.getProject() == null || task.getProject().getId() != projectId) {
            throw new RuntimeException("Task does not belong to this project");
        }

        task.setProject(null); // ⚠️ nullable = true
        taskRepository.save(task);
    }

}
