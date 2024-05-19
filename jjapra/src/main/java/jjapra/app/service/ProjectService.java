package jjapra.app.service;

import jjapra.app.dto.AddProjectRequest;
import jjapra.app.model.Project;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public Project save(AddProjectRequest request) {
        return projectRepository.save(request.toEntity());
    }

    public Project findById(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project findByTitle(String title) {
        return projectRepository.findByTitle(title).orElse(null);
    }
}
