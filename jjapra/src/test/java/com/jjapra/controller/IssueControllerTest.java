package com.jjapra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjapra.app.config.jwt.JwtMember;
import jjapra.app.controller.IssueController;
import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.IssueAssigneeService;
import jjapra.app.service.IssueService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = IssueController.class)
@AutoConfigureMockMvc
@WithMockUser
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueService issueService;

    @MockBean
    private ProjectMemberService projectMemberService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private JwtMember jwtMember;

    @MockBean
    private IssueAssigneeService issueAssigneeService;

    private ObjectMapper objectMapper;
    private Member adminMember;
    private Project project;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        objectMapper = new ObjectMapper();
        adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setRole(MemberRole.valueOf("ADMIN"));

        Member userMember = new Member();
        userMember.setId("user");
        userMember.setRole(MemberRole.valueOf("USER"));

        project = new Project();
        project.setId(1);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/**").with(csrf()))
                .defaultRequest(post("/**").with(csrf()))
                .defaultRequest(patch("/**").with(csrf()))
                .defaultRequest(delete("/**").with(csrf()))
                .defaultRequest(put("/**").with(csrf()))
                .build();
    }

    @Test
    void testAddIssue() throws Exception {
        AddIssueRequest addIssueRequest = new AddIssueRequest();
        addIssueRequest.setTitle("Test Issue");
        addIssueRequest.setDescription("Test Description");

        Issue savedIssue = new Issue();
        savedIssue.setProjectId(1);
        savedIssue.setIssueId(1);
        savedIssue.setTitle("Test Issue");
        savedIssue.setDescription("Test Description");

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(new ProjectMember()));
        Mockito.when(issueService.save(any(AddIssueRequest.class))).thenReturn(savedIssue);

        mockMvc.perform(post("/projects/1/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addIssueRequest))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(savedIssue.getTitle()))
                .andExpect(jsonPath("$.description").value(savedIssue.getDescription()));
    }

    @Test
    void testGetIssues() throws Exception {
        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test Description");

        List<Issue> issues = List.of(issue);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(issueService.findAll()).thenReturn(issues);

        mockMvc.perform(get("/issues")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueId").value(issue.getIssueId()))
                .andExpect(jsonPath("$[0].title").value(issue.getTitle()))
                .andExpect(jsonPath("$[0].description").value(issue.getDescription()));
    }

    @Test
    void testGetIssueDetails() throws Exception {
        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test Description");
        //이거 하니까 되네 하,,,
        issue.setProjectId(1);

        Project project = new Project();
        project.setId(1);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(adminMember);
        projectMember.setProject(project);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(issue));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));
        Mockito.when(issueAssigneeService.findByIssueId(anyInt())).thenReturn(Optional.empty());


        mockMvc.perform(get("/issues/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddComment() throws Exception {
        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("Test Comment");

        Comment savedComment = new Comment();
        savedComment.setCommentId(1);
        savedComment.setContent("Test Comment");

        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setProjectId(1);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(adminMember);
        projectMember.setProject(project);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(issue));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));
        Mockito.when(issueService.addComment(anyInt(), any(AddCommentRequest.class), anyString())).thenReturn(savedComment);

        mockMvc.perform(post("/issues/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCommentRequest))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(savedComment.getCommentId()))
                .andExpect(jsonPath("$.content").value(savedComment.getContent()));
    }

    @Test
    void testGetIssuesByProjectId() throws Exception {
        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test Description");

        List<Issue> issues = List.of(issue);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(adminMember);
        projectMember.setProject(project);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));
        Mockito.when(issueService.findByProjectId(anyInt())).thenReturn(issues);

        mockMvc.perform(get("/projects/1/issues")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issue.issueId").value(issue.getIssueId()))
                .andExpect(jsonPath("$[0].issue.title").value(issue.getTitle()))
                .andExpect(jsonPath("$[0].issue.description").value(issue.getDescription()));
    }

    @Test
    void testUpdateIssue() throws Exception {
        UpdateIssueRequest updateIssueRequest = new UpdateIssueRequest();
        updateIssueRequest.setTitle("Updated Title");
        updateIssueRequest.setDescription("Updated Description");

        Issue existingIssue = new Issue();
        existingIssue.setIssueId(1);
        existingIssue.setProjectId(1);

        Issue updatedIssue = new Issue();
        updatedIssue.setIssueId(1);
        updatedIssue.setTitle("Updated Title");
        updatedIssue.setDescription("Updated Description");

        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(adminMember);
        projectMember.setProject(project);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(existingIssue));
        Mockito.when(issueService.updateIssue(anyInt(), any(UpdateIssueRequest.class))).thenReturn(updatedIssue);
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));

        mockMvc.perform(patch("/issues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateIssueRequest))
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedIssue.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedIssue.getDescription()));
    }

    @Test
    void testDeleteIssue() throws Exception {
        Issue issue = new Issue();
        issue.setIssueId(1);
        issue.setProjectId(1);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setMember(adminMember);
        projectMember.setProject(project);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(issue));
        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
                .thenReturn(Optional.of(projectMember));

        Mockito.doNothing().when(issueService).deleteIssue(anyInt());

        mockMvc.perform(delete("/issues/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNoContent());
    }

}
