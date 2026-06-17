package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Config.SecurityUtils;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.Entity.User;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectTimeLineRepository;
import wevioo.example.resourcemanagementproject.Mapper.ProjectTimeLineMapper;
import wevioo.example.resourcemanagementproject.Validator.Impl.ProjectTimeLineValidator;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectTimeLineService {

    private final ProjectTimeLineRepository repository;
    private final ProjectRepository projectRepository;
    private final ProjectTimeLineMapper mapper;
    private final PaginationUtil paginationUtil;      // pour pagination
    private final SecurityUtils securityUtils;
    private final ProjectTimeLineValidator projectTimeLineValidator;  // ← inject



    // CREATE
    public ProjectTimeLineDTO create(ProjectTimeLineDTO dto) {
        securityUtils.requireAdminOrManager();
        projectTimeLineValidator.validateCreate(dto);

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        //  Manager → seulement ses projects
        if (securityUtils.isManager() &&
                project.getProjectManager().getId() != securityUtils.getCurrentUserId()) {
            throw new ResourceNotFoundException("Project not found");
        }

        ProjectTimeLine t = new ProjectTimeLine();
        mapper.updateProjectTimeLineEntity(dto, t);
        t.setProject(project);

        return mapper.ProjectTimeLineToProjectTimeLineDTO(repository.save(t));
    }

    // UPDATE  Admin + Manager (own projects)
    public ProjectTimeLineDTO update(Long id, ProjectTimeLineDTO dto) {

        securityUtils.requireAdminOrManager();
        projectTimeLineValidator.validateUpdate(dto);

        ProjectTimeLine t = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timeline not found"));

        // Manager → seulement ses projects
        if (securityUtils.isManager() &&
                t.getProject().getProjectManager().getId() != securityUtils.getCurrentUserId()) {
            throw new ResourceNotFoundException("Timeline not found");
        }

        mapper.updateProjectTimeLineEntity(dto, t);

        t.setUpdatedDate(LocalDateTime.now());

        if (dto.getProjectId() != null) {
            t.setProject(projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found")));
        }

        return mapper.ProjectTimeLineToProjectTimeLineDTO(repository.save(t));
    }

    //  GET ALL — يتبدل : page تبدأ من 1
    public PaginatedResponse<ProjectTimeLineDTO> getAll(Integer page, Integer pageSize, String sortBy, String sortDir) {
        CustomSort customSort = null;
        if (sortBy != null && sortDir != null) {
            customSort = new CustomSort();
            customSort.setColumnKey(sortBy);
            customSort.setOrder(Sort.Direction.fromString(sortDir));
        }

        Sort sorting = paginationUtil.sortingCriteria(customSort, Sort.Direction.ASC, "createdDate");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        //Page<ProjectTimeLine> ProjectTimeLinePage = repository.findAll(pageable);
        User currentUser = securityUtils.getCurrentUser();
        Page<ProjectTimeLine> projectTimelinePage;

        if (securityUtils.isAdmin()) {
            //  Admin → toutes
            projectTimelinePage = repository.findAll(pageable);

        } else if (securityUtils.isManager()) {
            //  Manager → timelines de ses projects
            projectTimelinePage = repository
                    .findByProject_ProjectManagerId(currentUser.getId(), pageable);

        } else {
            //  User → timelines des projects assignés
            projectTimelinePage = repository
                    .findByProject_UserProjects_UserId(currentUser.getId(), pageable);
        }

        PaginatedResponse<ProjectTimeLineDTO> response = new PaginatedResponse<>();
        response.setContent(projectTimelinePage.getContent().stream().map(mapper::ProjectTimeLineToProjectTimeLineDTO).toList());
        response.setPage(projectTimelinePage.getNumber() + 1);
        response.setPageSize(projectTimelinePage.getSize());
        response.setTotalElement(projectTimelinePage.getTotalElements());
        response.setTotalPage(projectTimelinePage.getTotalPages());
        return response;
    }


    // GET BY PROJECT
    public List<ProjectTimeLineDTO> getByProject(Long projectId) {
        return repository.findByProjectId(projectId)
                .stream()
                .map(mapper::ProjectTimeLineToProjectTimeLineDTO)
                .toList();
    }
    // ─── GET BY ID ───────────────────────────────────────
    public ProjectTimeLineDTO getById(Long id) {
        ProjectTimeLine timeline = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timeline not found"));

        User currentUser = securityUtils.getCurrentUser();

        if (securityUtils.isManager() &&
                timeline.getProject().getProjectManager().getId() != currentUser.getId()) {
            throw new ResourceNotFoundException("Timeline not found");
        }

        return mapper.ProjectTimeLineToProjectTimeLineDTO(timeline);
    }


    //  SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
    public PaginatedResponse<ProjectTimeLineDTO> searchProjectTimeLines(
            String title,
            String description,
            String version,
            ProjectTimeLineType type,
            Boolean deliveredToClient,
            Long projectId,
            String name,
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

        Page<ProjectTimeLine> ProjectTimeLinePage = repository.searchProjectTimeLines(
                normalize(title),
                normalize(description),
                normalize(version),
                type,
                deliveredToClient,
                projectId,
                normalize(name),
                progressPercent,
                pageable
        );

        // ← البناء الجديد للـ response
        PaginatedResponse<ProjectTimeLineDTO> response = new PaginatedResponse<>();
        response.setContent(ProjectTimeLinePage.getContent().stream()
                .map(mapper::ProjectTimeLineToProjectTimeLineDTO)
                .toList());
        response.setPage(ProjectTimeLinePage.getNumber() + 1);   // Spring 0-indexed → on remet à 1
        response.setPageSize(ProjectTimeLinePage.getSize());
        response.setTotalElement(ProjectTimeLinePage.getTotalElements());
        response.setTotalPage(ProjectTimeLinePage.getTotalPages());

        return response;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    // DELETE
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }

    // ─── DELETE ──────────────────────────────────────────
    public void delete(Long id) {
        securityUtils.requireAdminOrManager();
        ProjectTimeLine timeline = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timeline not found"));

        // Manager → seulement ses projects
        if (securityUtils.isManager() &&
                timeline.getProject().getProjectManager().getId() != securityUtils.getCurrentUserId()) {
            throw new ResourceNotFoundException("Timeline not found");
        }

        repository.deleteById(id);
    }
}
