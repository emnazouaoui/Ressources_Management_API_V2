package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.DepartmentDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Entity.Department;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginatedResponse;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectTimeLineRepository;
import wevioo.example.resourcemanagementproject.Mapper.ProjectTimeLineMapper;


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


    // CREATE
    public ProjectTimeLineDTO create(ProjectTimeLineDTO dto) {

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectTimeLine t = new ProjectTimeLine();
        mapper.updateEntity(dto, t);

        t.setProject(project);

        if (t.getProgressPercent() == null) {
            t.setProgressPercent(null);
        }

        return mapper.toDTO(repository.save(t));
    }

    // UPDATE
    public ProjectTimeLineDTO update(Long id, ProjectTimeLineDTO dto) {

        ProjectTimeLine t = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Timeline not found"));

        mapper.updateEntity(dto, t);

        t.setUpdatedDate(LocalDateTime.now());

        if (dto.getProjectId() != null) {
            t.setProject(projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found")));
        }

        return mapper.toDTO(repository.save(t));
    }

//    //  GET ALL — يتبدل : page تبدأ من 1
//    public Page<ProjectTimeLineDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//        return repository.findAll(pageable).map(mapper::toDTO);
//    }
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

        Page<ProjectTimeLine> ProjectTimeLinePage = repository.findAll(pageable);

        PaginatedResponse<ProjectTimeLineDTO> response = new PaginatedResponse<>();
        response.setContent(ProjectTimeLinePage.getContent().stream().map(mapper::toDTO).toList());
        response.setPage(ProjectTimeLinePage.getNumber() + 1);
        response.setPageSize(ProjectTimeLinePage.getSize());
        response.setTotalElement(ProjectTimeLinePage.getTotalElements());
        response.setTotalPage(ProjectTimeLinePage.getTotalPages());
        return response;
    }


    // GET BY PROJECT
    public List<ProjectTimeLineDTO> getByProject(Long projectId) {
        return repository.findByProjectId(projectId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


//    //  SEARCH
//    public Page<ProjectTimeLineDTO> searchProjectTimeLines(
//            String title,
//            String description,
//            String version,
//            ProjectTimeLineType type,
//            Boolean deliveredToClient,
//            Long projectId,
//            String name,
//            Double progressPercent,
//            Integer page,
//            Integer pageSize,
//            CustomSort sort
//    ) {
//        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
//        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
//
//        return repository.searchProjectTimeLines(
//                normalize(title),
//                normalize(description),
//                normalize(version),
//                type,
//                deliveredToClient,
//                projectId,
//                normalize(name),
//                progressPercent,
//                pageable
//        ).map(mapper::toDTO);
//    }

    // ✅ SEARCH — retourne PaginatedResponse au lieu de Page<ClientDTO>
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
                .map(mapper::toDTO)
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
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
