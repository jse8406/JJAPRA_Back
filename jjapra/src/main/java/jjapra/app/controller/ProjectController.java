package jjapra.app.controller;

import jjapra.app.dto.AddProjectRequest;
import jjapra.app.model.Project;
import jjapra.app.service.ProjectService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("")
    public List<Project> getProjects() {
        return projectService.findAll();
    }

    @PostMapping("")
    public ResponseEntity<Project> addProject(@RequestBody AddProjectRequest request) {
        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project project = projectService.findByTitle(request.getTitle());
        if (project != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project savedProject = projectService.save(request);
        System.out.println("Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id) {
        Project project = projectService.findById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request) {
        Project updatedProject = projectService.update(id, request);
        if (updatedProject == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }
}

