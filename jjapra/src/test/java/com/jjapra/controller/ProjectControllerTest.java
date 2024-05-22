package com.jjapra.controller;

import jjapra.app.controller.ProjectController;
import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.model.member.Role;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProjectController.class)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectMemberService projectMemberService;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        session = new MockHttpSession();
        Member loggedInUser = new Member();
        loggedInUser.setId("test1");
        session.setAttribute("loggedInUser", loggedInUser);
    }

//    @DisplayName("Success get all projects")
//    @Test
//    public void testGetProjects() throws Exception {
//        List<Project> projects = new ArrayList<>();
//        Project project = new Project();
//        project.setId(1);
//        project.setTitle("Test Project");
//        projects.add(project);
//
//        when(projectService.findAll()).thenReturn(projects);
//
//        mockMvc.perform(get("/projects")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(project.getId()))
//                .andExpect(jsonPath("$[0].title").value(project.getTitle()));
//    }

    @DisplayName("Success add project")
    @Test
    public void testAddProject() throws Exception {
        AddProjectRequest request = new AddProjectRequest();
        request.setTitle("New Project");

        Project project = new Project();
        project.setId(1);
        project.setTitle("New Project");

        when(projectService.save(any(AddProjectRequest.class))).thenReturn(project);
        when(projectService.findByTitle(request.getTitle())).thenReturn(null);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Project\"}")
                        .session(session))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value(project.getTitle()));
    }

    @DisplayName("Fail add project with existing title")
    @Test
    public void testAddProjectWithExistingTitle() throws Exception {
        AddProjectRequest request = new AddProjectRequest();
        request.setTitle("Existing Project");

        Project project = new Project();
        project.setId(1);
        project.setTitle("Existing Project");

        when(projectService.findByTitle(request.getTitle())).thenReturn(project);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Existing Project\"}")
                        .session(session))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Success get project by id")
    @Test
    public void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setTitle("Test Project");

        when(projectService.findById(1)).thenReturn(project);

        mockMvc.perform(get("/projects/1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value(project.getTitle()));
    }

    @DisplayName("Fail get project by id")
    @Test
    public void testGetProjectByIdNotFound() throws Exception {
        when(projectService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/projects/1")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Success update project")
    @Test
    public void testUpdateProject() throws Exception {
        AddProjectRequest request = new AddProjectRequest();
        request.setTitle("Updated Project");

        Project project = new Project();
        project.setId(1);
        project.setTitle("Updated Project");

        when(projectService.update(any(Integer.class), any(AddProjectRequest.class))).thenReturn(project);

        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Project\"}")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value(project.getTitle()));
    }

    @DisplayName("Fail update project")
    @Test
    public void testUpdateProjectNotFound() throws Exception {
        AddProjectRequest request = new AddProjectRequest();
        request.setTitle("Updated Project");

        when(projectService.update(any(Integer.class), any(AddProjectRequest.class))).thenReturn(null);

        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Project\"}")
                        .session(session))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Success delete project")
    @Test
    public void testDeleteProject() throws Exception {
        when(projectService.deleteProject(1)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        mockMvc.perform(delete("/projects/1")
                        .session(session))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Fail delete project")
    @Test
    public void testDeleteProjectNotFound() throws Exception {
        when(projectService.deleteProject(1)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(delete("/projects/1")
                        .session(session))
                .andExpect(status().isNotFound());
    }
}
