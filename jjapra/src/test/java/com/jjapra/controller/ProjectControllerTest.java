package com.jjapra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjapra.app.config.jwt.JwtMember;
import jjapra.app.controller.ProjectController;
import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.MemberService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProjectController.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectMemberService projectMemberService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtMember jwtMember;

    private ObjectMapper objectMapper;
    private Member adminMember;
    private Member userMember;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();

        adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setRole(MemberRole.ADMIN);

        userMember = new Member();
        userMember.setId("user");
        userMember.setRole(MemberRole.USER);
    }

    @DisplayName("Success get all projects")
    @Test
    public void testGetProjects() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setTitle("Test Project");

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setMember(adminMember);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectMemberService.findAll()).thenReturn(List.of(projectMember));

        mockMvc.perform(get("/projects")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].project.id").value(project.getId()))
                .andExpect(jsonPath("$[0].project.title").value(project.getTitle()));
    }

    @DisplayName("Success add project")
    @Test
    public void testAddProject() throws Exception {
        AddProjectRequest request = new AddProjectRequest();
        request.setTitle("New Project");

        Project project = new Project();
        project.setId(1);
        project.setTitle("New Project");

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findByTitle(request.getTitle())).thenReturn(null);
        Mockito.when(projectService.save(any(AddProjectRequest.class))).thenReturn(project);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
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

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findByTitle(request.getTitle())).thenReturn(project);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Success get project by id")
    @Test
    public void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setTitle("Test Project");

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(1)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(project.getId()))
                .andExpect(jsonPath("$.title").value(project.getTitle()));
    }

    @DisplayName("Fail get project by id")
    @Test
    public void testGetProjectByIdNotFound() throws Exception {
        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
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

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.update(anyInt(), any(AddProjectRequest.class))).thenReturn(project);

        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
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

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.update(anyInt(), any(AddProjectRequest.class))).thenReturn(null);

        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Success delete project")
    @Test
    public void testDeleteProject() throws Exception {
        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.deleteProject(1)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());

        mockMvc.perform(delete("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Fail delete project")
    @Test
    public void testDeleteProjectNotFound() throws Exception {
        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.deleteProject(1)).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        mockMvc.perform(delete("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
