//package com.jjapra.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jjapra.app.config.jwt.JwtMember;
//import jjapra.app.controller.IssueMemberController;
//import jjapra.app.dto.issue.AddIssueMemberRequest;
//import jjapra.app.model.issue.Issue;
//import jjapra.app.model.issueMember.IssueAssignee;
//import jjapra.app.model.issueMember.IssueFixer;
//import jjapra.app.model.member.Member;
//import jjapra.app.model.member.MemberRole;
//import jjapra.app.model.project.Project;
//import jjapra.app.model.project.ProjectMember;
//import jjapra.app.service.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(classes = IssueMemberController.class)
//@AutoConfigureMockMvc
//public class IssueMemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IssueFixerService issueFixerService;
//
//    @MockBean
//    private IssueAssigneeService issueAssigneeService;
//
//    @MockBean
//    private IssueService issueService;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private ProjectService projectService;
//
//    @MockBean
//    private ProjectMemberService projectMemberService;
//
//    @MockBean
//    private JwtMember jwtMember;
//
//    private ObjectMapper objectMapper;
//    private Member plMember;
//    private Project project;
//    private Issue issue;
//
//    @BeforeEach
//    void setUp() {
//        objectMapper = new ObjectMapper();
//
//        plMember = new Member();
//        plMember.setId("plMember");
//        plMember.setRole(MemberRole.valueOf("PL"));
//
//        Member userMember = new Member();
//        userMember.setId("user");
//        userMember.setRole(MemberRole.valueOf("USER"));
//
//        project = new Project();
//        project.setId(1);
//
//        issue = new Issue();
//        issue.setIssueId(1);
//        issue.setProjectId(1);
//    }
//
//    @DisplayName("Success save Issue Fixer")
//    @Test
//    void testSaveIssueFixer() throws Exception {
//        AddIssueMemberRequest request = new AddIssueMemberRequest();
//        request.setId("user");
//        request.setRole("FIXER");
//
//        Member member = new Member();
//        member.setId("user");
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(plMember));
//        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(issue));
//        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
//        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
//                .thenReturn(Optional.of(new ProjectMember()));
//        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));
//        Mockito.when(issueFixerService.save(any(IssueFixer.class))).thenReturn(new IssueFixer(issue, member));
//
//        mockMvc.perform(post("/issues/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isCreated());
//    }
//
//    @DisplayName("Success save Issue Assignee")
//    @Test
//    void testSaveIssueAssignee() throws Exception {
//        AddIssueMemberRequest request = new AddIssueMemberRequest();
//        request.setId("user");
//        request.setRole("ASSIGNEE");
//
//        Member member = new Member();
//        member.setId("user");
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(plMember));
//        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.of(issue));
//        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
//        Mockito.when(projectMemberService.findByProjectAndMember(any(Project.class), any(Member.class)))
//                .thenReturn(Optional.of(new ProjectMember()));
//        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));
//        Mockito.when(issueAssigneeService.save(any(IssueAssignee.class))).thenReturn(new IssueAssignee(issue, member));
//
//        mockMvc.perform(post("/issues/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isCreated());
//    }
//
//    @DisplayName("Fail save Issue Member with unauthorized user")
//    @Test
//    void testSaveIssueMemberUnauthorized() throws Exception {
//        AddIssueMemberRequest request = new AddIssueMemberRequest();
//        request.setId("user");
//        request.setRole("FIXER");
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/issues/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @DisplayName("Fail save Issue Member with invalid issue ID")
//    @Test
//    void testSaveIssueMemberInvalidIssue() throws Exception {
//        AddIssueMemberRequest request = new AddIssueMemberRequest();
//        request.setId("user");
//        request.setRole("FIXER");
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(plMember));
//        Mockito.when(issueService.findById(anyInt())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/issues/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//}
