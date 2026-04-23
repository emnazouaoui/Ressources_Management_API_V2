package wevioo.example.resourcemanagementproject.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.DTO.ProjectDTO;
import wevioo.example.resourcemanagementproject.Entity.*;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Mapper.ProjectMapper;
import wevioo.example.resourcemanagementproject.Repository.*;

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
    private final ProjectTechnologyRepository projectTechnologyRepository;

    private final ProjectMapper mapper;

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

    // 🔥 UPDATE
    public ProjectDTO update(Long id, ProjectDTO dto) {

        Project p = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        mapper.updateEntity(dto, p);

        p.setStatus(ProjectStatus.valueOf(dto.getStatus()));

        p.setProjectManager(userRepository.findById(dto.getProjectManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found")));

        p.setClient(clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found")));

        Project saved = projectRepository.save(p);

        return mapper.toDTO(saved);
    }

    // 🔥 GET ALL
    public List<ProjectDTO> getAll() {
        return projectRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    // 🔥 SEARCH
    public List<ProjectDTO> search(String keyword) {
        return projectRepository.search(keyword)
                .stream().map(mapper::toDTO).toList();
    }

    // 🔥 DELETE
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    // =========================
    // 🔥 RELATIONS
    // =========================

    public void assignTechnologies(Long projectId, List<Long> techIds) {
        if (techIds == null) return;

        for (Long techId : techIds) {

            if (projectTechnologyRepository.existsByProjectIdAndTechnologyId(projectId, techId))
                continue;

            ProjectTechnology pt = new ProjectTechnology();

            Project p = new Project();
            p.setId(projectId);

            Technology t = technologyRepository.findById(techId)
                    .orElseThrow(() -> new RuntimeException("Tech not found"));

            pt.setProject(p);
            pt.setTechnology(t);

            projectTechnologyRepository.save(pt);
        }
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

    // remove
    public void removeTechnology(Long projectId, Long techId) {
        projectTechnologyRepository.deleteByProjectIdAndTechnologyId(projectId, techId);
    }

    public void removeUser(Long projectId, Long userId) {
        userProjectRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

}
