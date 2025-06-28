package main.processminig.service;

import main.processminig.domain.Project;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

@Service
public class ProjectService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS project (id TEXT PRIMARY KEY, name TEXT, description TEXT, createdDate TEXT, createdBy TEXT, updatedBy TEXT)");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS process_log (id INTEGER PRIMARY KEY AUTOINCREMENT, project_id TEXT, log_data TEXT, FOREIGN KEY(project_id) REFERENCES project(id))");
    }

    public List<Project> getAllProjects() {
        return jdbcTemplate.query("SELECT * FROM project", new ProjectRowMapper());
    }

    public Project getProjectById(String id) {
        Project project = jdbcTemplate.queryForObject("SELECT * FROM project WHERE id = ?", new Object[]{id}, new ProjectRowMapper());
        // Fetch process logs for this project
        List<String> logs = jdbcTemplate.query("SELECT log_data FROM process_log WHERE project_id = ?", new Object[]{id},
            (rs, rowNum) -> rs.getString("log_data")
        );
        if (!logs.isEmpty()) {
            // If you want to return the latest log, or concatenate, adjust as needed
            project.setPdata(logs.get(logs.size() - 1));
        }
        return project;
    }

    public Project saveProject(Project project) {
        boolean isInsert = (project.getId() == null || project.getId().trim().isEmpty());
        String projectId = project.getId();

        if (isInsert) {
            // Generate a new UUID for the project id
            projectId = java.util.UUID.randomUUID().toString();
            project.setId(projectId);
            jdbcTemplate.update(
                "INSERT INTO project (id, name, description, createdDate, createdBy, updatedBy) VALUES (?, ?, ?, ?, ?, ?)",
                projectId,
                project.getName(),
                project.getProcess(),
                project.getCreationDate() != null ? project.getCreationDate().toString() : null,
                project.getCreatedBy(),
                project.getUpdatedBy()
            );
        } else {
            jdbcTemplate.update(
                "UPDATE project SET name = ?, description = ?, createdDate = ?, createdBy = ?, updatedBy = ? WHERE id = ?",
                project.getName(),
                project.getProcess(),
                project.getCreationDate() != null ? project.getCreationDate().toString() : null,
                project.getCreatedBy(),
                project.getUpdatedBy(),
                projectId
            );
        }
        // Save logs if present
        if (project.getPdata() != null) {
            jdbcTemplate.update(
                "INSERT INTO process_log (project_id, log_data) VALUES (?, ?)",
                projectId,
                project.getPdata()
            );
        }
        return project;
    }

    private static class ProjectRowMapper implements RowMapper<Project> {
        @Override
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            Project p = new Project();
            p.setId(rs.getString("id"));
            p.setName(rs.getString("name"));
            p.setProcess(rs.getString("description"));
            p.setCreatedBy(rs.getString("createdBy"));
            p.setUpdatedBy(rs.getString("updatedBy"));
            // You may want to parse createdDate to Date if needed
            return p;
        }
    }
}
