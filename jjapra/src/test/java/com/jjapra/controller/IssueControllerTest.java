//package com.jjapra.controller;
//
//import jjapra.app.controller.IssueController;
//import jjapra.app.dto.issue.AddCommentRequest;
//import jjapra.app.dto.issue.AddIssueRequest;
//import jjapra.app.dto.issue.UpdateIssueRequest;
//import jjapra.app.model.issue.Comment;
//import jjapra.app.model.issue.Issue;
//import jjapra.app.model.member.Member;
//import jjapra.app.model.project.Project;
//import jjapra.app.model.project.ProjectMember;
//import jjapra.app.service.IssueService;
//import jjapra.app.service.ProjectMemberService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(classes = IssueController.class)
//@AutoConfigureMockMvc
//public class IssueControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IssueService issueService;
//
//    @MockBean
//    private ProjectMemberService projectMemberService;
//
//    private MockHttpSession session;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        session = new MockHttpSession();
//        Member loggedInUser = new Member();
//        loggedInUser.setId("test1");
//        session.setAttribute("loggedInUser", loggedInUser);
//    }
//
//    @DisplayName("Success add issue")
//    @Test
//    public void testAddIssue() throws Exception {
//        AddIssueRequest request = new AddIssueRequest();
//        request.setTitle("Test Issue");
//        request.setDescription("Test Description");
//
//        Issue issue = new Issue();
//        issue.setIssueId(1);
//        issue.setTitle("Test Issue");
//        issue.setDescription("Test Description");
//
//        when(issueService.save(any(AddIssueRequest.class))).thenReturn(issue);
//
//        mockMvc.perform(post("/issues")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"Test Issue\", \"description\":\"Test Description\"}")
//                        .session(session))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.issueId").value(issue.getIssueId()))
//                .andExpect(jsonPath("$.title").value(issue.getTitle()))
//                .andExpect(jsonPath("$.description").value(issue.getDescription()));
//    }
//
//    @DisplayName("Success get issue")
//    @Test
//    public void testGetIssues() throws Exception {
//        List<ProjectMember> projectMembers = new ArrayList<>();
//        Project project = new Project();
//        project.setId(1);
//        ProjectMember projectMember = new ProjectMember();
//        projectMember.setProject(project);
//        projectMembers.add(projectMember);
//
//        when(projectMemberService.findByMemberId("test1")).thenReturn(projectMembers);
//
//        List<Issue> issues = new ArrayList<>();
//        Issue issue = new Issue();
//        issue.setIssueId(1);
//        issue.setProjectId(1);
//        issues.add(issue);
//
//        when(issueService.findAll()).thenReturn(issues);
//
//        mockMvc.perform(get("/issues")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].issueId").value(issue.getIssueId()));
//    }
//
//    @DisplayName("Success get issue details")
//    @Test
//    public void testGetIssueDetails() throws Exception {
//        Issue issue = new Issue();
//        issue.setIssueId(1);
//        issue.setTitle("Test Issue");
//        issue.setDescription("Test Description");
//
//        List<Comment> comments = new ArrayList<>();
//
//        when(issueService.findById(1)).thenReturn(Optional.of(issue));
//        when(issueService.findCommentsByIssueId(1)).thenReturn(comments);
//
//        mockMvc.perform(get("/issues/details/1")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.title").value(issue.getTitle()))
//                .andExpect(jsonPath("$.description").value(issue.getDescription()));
//    }
//
//    @DisplayName("Success add comment")
//    @Test
//    public void testAddComment() throws Exception {
//        AddCommentRequest request = new AddCommentRequest();
//        request.setContent("Test Comment");
//
//        Comment comment = new Comment();
//        comment.setContent("Test Comment");
//
//        when(issueService.addComment(any(Integer.class), any(AddCommentRequest.class))).thenReturn(comment);
//
//        mockMvc.perform(post("/comments/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"content\":\"Test Comment\"}")
//                        .session(session))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.content").value(comment.getContent()));
//    }
//
//    @DisplayName("Success update issue")
//    @Test
//    public void testUpdateIssue() throws Exception {
//        UpdateIssueRequest request = new UpdateIssueRequest();
//        request.setTitle("Updated Title");
//
//        Issue issue = new Issue();
//        issue.setTitle("Updated Title");
//
//        when(issueService.updateIssue(any(Integer.class), any(UpdateIssueRequest.class))).thenReturn(issue);
//
//        mockMvc.perform(patch("/issues/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"Updated Title\"}")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.title").value(issue.getTitle()));
//    }
//
//    @DisplayName("Success delete issue")
//    @Test
//    public void testDeleteIssue() throws Exception {
//        mockMvc.perform(delete("/issues/1")
//                        .session(session))
//                .andExpect(status().isNoContent());
//    }
//}
