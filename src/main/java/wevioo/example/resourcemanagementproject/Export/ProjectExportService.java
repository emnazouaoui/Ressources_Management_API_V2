package wevioo.example.resourcemanagementproject.Export;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wevioo.example.resourcemanagementproject.Entity.Project;
import wevioo.example.resourcemanagementproject.Enums.ProjectStatus;
import wevioo.example.resourcemanagementproject.Repository.ProjectRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectExportService {

    private final ProjectRepository projectRepository;
    private final ExcelExportService excelExportService;

    public byte[] exportProjects(
            String name,
            String description,
            ProjectStatus status,
            Long projectManagerId,
            String projectManagerUsername,
            Long clientId,
            String clientName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Double progressPercent
    ) throws IOException {

        // ─── Fetch data avec filtres — sans pagination ───────────
        List<Project> projects = projectRepository.searchProjectsForExport(
                normalize(name),
                normalize(description),
                status,
                projectManagerId,
                normalize(projectManagerUsername),
                clientId,
                normalize(clientName),
                startDate,
                endDate,
                progressPercent
        );

        // ─── Headers ────────────────────────────────────────────
        List<String> headers = List.of(
                "ID", "Name", "Description", "Status",
                "Progress %", "Start Date", "End Date",
                "Project Manager", "Client"
        );

        // ─── Rows ────────────────────────────────────────────────
        List<List<String>> rows = projects.stream()
                .map(p -> List.of(
                        String.valueOf(p.getId()),
                        safe(p.getName()),
                        safe(p.getDescription()),
                        p.getStatus() != null ? p.getStatus().name() : "",
                        p.getProgressPercent() != null ? p.getProgressPercent() + "%" : "",
                        p.getStartDate() != null ? p.getStartDate().toString() : "",
                        p.getEndDate() != null ? p.getEndDate().toString() : "",
                        p.getProjectManager() != null ? p.getProjectManager().getUsername() : "",
                        p.getClient() != null ? p.getClient().getName() : ""
                ))
                .toList();

        return excelExportService.export("Projects", headers, rows);
    }

    private String normalize(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

}
