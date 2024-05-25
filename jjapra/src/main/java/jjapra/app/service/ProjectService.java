package jjapra.app.service;

import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.project.Project;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project save(AddProjectRequest request) {
        return projectRepository.save(request.toEntity());
    }
    public Optional<Project> findById(Integer id) {
        return projectRepository.findById(id);
    }

    public Project findByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }

    public Project update(Integer id, AddProjectRequest request) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return null;
        }
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        return project;
    }

    public ResponseEntity<Project> deleteProject(Integer id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }
}
