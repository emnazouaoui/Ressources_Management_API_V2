package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ClientDTO;
import wevioo.example.resourcemanagementproject.DTO.ProjectTimeLineDTO;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Entity.ProjectTimeLine;
import wevioo.example.resourcemanagementproject.Enums.ProjectTimeLineType;
import wevioo.example.resourcemanagementproject.Pagination.CustomSort;
import wevioo.example.resourcemanagementproject.Pagination.PaginationUtil;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;
import wevioo.example.resourcemanagementproject.Repository.ProjectTimeLineRepository;
import wevioo.example.resourcemanagementproject.Mapper.ProjectTimeLineMapper;


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

        if (dto.getProjectId() != null) {
            t.setProject(projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found")));
        }

        return mapper.toDTO(repository.save(t));
    }

//    // GET ALL
//    public List<ProjectTimeLineDTO> getAll() {
//        return repository.findAll()
//                .stream()
//                .map(mapper::toDTO)
//                .toList();
//    }

//    public Page<ProjectTimeLineDTO> getAll(int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        return repository.findAll(pageable)
//                .map(mapper::toDTO);
//    }
    //  GET ALL — يتبدل : page تبدأ من 1
    public Page<ProjectTimeLineDTO> getAll(Integer page, Integer pageSize, CustomSort sort) {
        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);
        return repository.findAll(pageable).map(mapper::toDTO);
    }


    // GET BY PROJECT
    public List<ProjectTimeLineDTO> getByProject(Long projectId) {
        return repository.findByProjectId(projectId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

//    // SEARCH
//    public List<ProjectTimeLineDTO> search(String keyword) {
//        return repository.search(keyword)
//                .stream()
//                .map(mapper::toDTO)
//                .toList();
//    }

    //  SEARCH
    public Page<ProjectTimeLineDTO> searchProjectTimeLines(
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
            CustomSort sort
    ) {
        Sort sorting = paginationUtil.sortingCriteria(sort, Sort.Direction.ASC, "name");
        Pageable pageable = paginationUtil.createPageable(page, pageSize, sorting);

        return repository.searchProjectTimeLines(
                normalize(title),
                normalize(description),
                normalize(version),
                type,
                deliveredToClient,
                projectId,
                normalize(name),
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

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
