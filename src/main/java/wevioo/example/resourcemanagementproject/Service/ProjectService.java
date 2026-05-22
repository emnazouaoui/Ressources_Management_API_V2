package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.Entity.Technology;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Entity.Task;
import wevioo.example.resourcemanagementproject.Entity.UserProject;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Mapper.ProjectMapper;
import wevioo.example.resourcemanagementproject.Repository.ClientRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectTimeLineRepository;
import wevioo.example.resourcemanagementproject.Repository.TaskRepository;
import wevioo.example.resourcemanagementproject.Repository.TechnologyRepository;
import wevioo.example.resourcemanagementproject.Repository.UserProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.UserRepository;


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

    private final PaginationUtil paginationUtil;      // pour pagination

    private final ProjectMapper mapper;


    // 🔥 CREATE
    public ProjectDTO create(ProjectDTO dto) {

        Project p = new Project();
        mapper.updateProjectEntity(dto, p);

        p.setStatus(ProjectStatus.valueOf(dto.getStatus()));

        p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));

        p.setClient(clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found")));

        Project saved = projectRepository.save(p);

        // assign relations
        assignUsers(saved.getId(), dto.getUserIds());
        assignTechnologies(saved.getId(), dto.getTechnologyIds());

        return mapper.ProjectToProjectDTO(saved);
    }

    // ✅ Après — plus de code history manuel !
    public ProjectDTO update(Long id, ProjectDTO dto) {

        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        mapper.updateProjectEntity(dto, p);

        if (dto.getStatus() != null) {
            p.setStatus(ProjectStatus.valueOf(dto.getStatus()));
        }

       // p.setUpdatedDate(LocalDateTime.now());

        p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found")));

        p.setClient(clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found")));

        // ← @PreUpdate يتكفل بالـ history تلقائياً !
        // ✅ Listener يتكفل بالـ history تلقائياً
        Project saved = projectRepository.save(p);

        return mapper.ProjectToProjectDTO(saved);
        //return mapper.toDTO(projectRepository.save(p));
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<ProjectDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Project> ProjectPage = projectRepository.findAll(pageable);

        PaginatedResponse<ProjectDTO> response = new PaginatedResponse<>();
        response.setContent(ProjectPage.getContent().stream().map(mapper::ProjectToProjectDTO).toList());
        response.setPage(ProjectPage.getNumber() + 1);
        response.setPageSize(ProjectPage.getSize());
        response.setTotalElement(ProjectPage.getTotalElements());
        response.setTotalPage(ProjectPage.getTotalPages());
        return response;
    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<ProjectDTO> searchProjects(
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
            Integer page,
            Integer pageSize,
            String sortBy,
            String sortDir
    ) {
        // ← بدل Sort.by(sortBy).ascending() مباشرة
        // نبني CustomSort ونمرروه لـ PaginationUtil بش يvalidiha
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(
                customSort,
                Sort.Direction.ASC,
                "createdDate"                  // ← default si sort == null
        );

        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        Page<Project> ProjectPage = projectRepository.searchProjects(
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
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<ProjectDTO> response = new PaginatedResponse<>();
        response.setContent(ProjectPage.getContent().stream()
                .map(mapper::ProjectToProjectDTO)
                .toList());
        response.setPage(ProjectPage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(ProjectPage.getSize());
        response.setTotalElement(ProjectPage.getTotalElements());
        response.setTotalPage(ProjectPage.getTotalPages());

        return response;
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


    // ✅ Après
    public void assignTechnologies(Long projectId, List<Long> techIds) {
        if (techIds == null) return;
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

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
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            up.setProject(p);
            up.setUser(u);

            userProjectRepository.save(up);
        }
    }

    public void addTimeline(Long projectId, Long timelineId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectTimeLine timeline = projectTimeLineRepository.findById(timelineId)
                .orElseThrow(() -> new ResourceNotFoundException("Timeline not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setProject(project);
        taskRepository.save(task);
    }



    // remove
//    public void removeTechnology(Long projectId, Long techId) {
//        projectTechnologyRepository.deleteByProjectIdAndTechnologyId(projectId, techId);
//    }

    public void removeTechnology(Long projectId, Long techId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        project.getTechnologies().removeIf(t -> t.getId() == techId);
        projectRepository.save(project);
    }

    public void removeUser(Long projectId, Long userId) {
        userProjectRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    public void removeTimeline(Long projectId, Long timelineId) {

        ProjectTimeLine timeline = projectTimeLineRepository.findById(timelineId)
                .orElseThrow(() -> new ResourceNotFoundException("Timeline not found"));

        if (timeline.getProject() == null ||
                timeline.getProject().getId() != projectId) {
            throw new RuntimeException("Timeline not linked to this project");
        }

        projectTimeLineRepository.delete(timeline);
    }

    // 🔥 REMOVE TASK (with validation)
    public void removeTask(Long projectId, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // ✅ check task belongs to project
        if (task.getProject() == null || task.getProject().getId() != projectId) {
            throw new RuntimeException("Task does not belong to this project");
        }

        task.setProject(null); // ⚠️ nullable = true
        taskRepository.save(task);
    }

}
