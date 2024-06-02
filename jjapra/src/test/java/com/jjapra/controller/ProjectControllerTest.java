package com.jjapra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjapra.app.config.jwt.JwtMember;
import jjapra.app.controller.ProjectController;
import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.IssueService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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
    private IssueService issueService;

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setMember(adminMember);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));

        mockMvc.perform(get("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.project.id").value(project.getId()))
                .andExpect(jsonPath("$.project.title").value(project.getTitle()));
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

    @DisplayName("Success delete project")
    @Test
    public void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setId(1);
        project.setTitle("Test Project");

        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setProjectId(1);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(issueService.findCommentByProjectId(anyInt())).thenReturn(List.of());
        Mockito.when(issueService.findByProjectId(anyInt())).thenReturn(List.of(issue));
        Mockito.when(projectMemberService.findByProject(any(Project.class))).thenReturn(List.of());

        mockMvc.perform(delete("/projects/1")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk());
    }




}
