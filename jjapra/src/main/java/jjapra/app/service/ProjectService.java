package jjapra.app.service;

import jjapra.app.dto.AddProjectRequest;
import jjapra.app.model.Project;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Project findById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project findByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }
}
