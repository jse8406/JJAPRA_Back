//package com.jjapra.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jjapra.app.config.jwt.JwtMember;
//import jjapra.app.controller.ProjectMemberController;
//import jjapra.app.dto.project.AddProjectMemberRequest;
//import jjapra.app.model.member.Member;
//import jjapra.app.model.member.MemberRole;
//import jjapra.app.model.project.Project;
//import jjapra.app.model.project.ProjectMember;
//import jjapra.app.service.MemberService;
//import jjapra.app.service.ProjectMemberService;
//import jjapra.app.service.ProjectService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
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
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest(classes = ProjectMemberController.class)
//@AutoConfigureMockMvc
//@WithMockUser
//public class ProjectMemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProjectMemberService projectMemberService;
//
//    @MockBean
//    private ProjectService projectService;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private JwtMember jwtMember;
//
//    private ObjectMapper objectMapper;
//    private Member adminMember;
//
//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//
//        adminMember = new Member();
//        adminMember.setId("admin");
//        adminMember.setRole(MemberRole.ADMIN);
//    }
//
//    @DisplayName("Success save project member")
//    @Test
//    public void testSaveProjectMember() throws Exception {
//        AddProjectMemberRequest request = new AddProjectMemberRequest();
//        request.setId("testMemberId");
//        request.setRole("TESTER");
//
//        Project project = new Project();
//        project.setId(1);
//
//        Member member = new Member();
//        member.setId("testMemberId");
//
//        ProjectMember projectMember = new ProjectMember();
//        projectMember.setProject(project);
//        projectMember.setMember(member);
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
//        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
//        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));
//        Mockito.when(projectMemberService.save(any(ProjectMember.class))).thenReturn(projectMember);
//
//        mockMvc.perform(post("/projects/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.project.id").value(project.getId()))
//                .andExpect(jsonPath("$.member.id").value(member.getId()));
//    }
//
//    @DisplayName("Fail save project member with non-existing project")
//    @Test
//    public void testSaveProjectMemberNonExistingProject() throws Exception {
//        AddProjectMemberRequest request = new AddProjectMemberRequest();
//        request.setId("testMemberId");
//        request.setRole("USER");
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
//        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/projects/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//
//    @DisplayName("Fail save project member with non-existing member")
//    @Test
//    public void testSaveProjectMemberNonExistingMember() throws Exception {
//        AddProjectMemberRequest request = new AddProjectMemberRequest();
//        request.setId("testMemberId");
//        request.setRole("USER");
//
//        Project project = new Project();
//        project.setId(1);
//
//        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
//        Mockito.when(projectService.findById(anyInt())).thenReturn(Optional.of(project));
//        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/projects/1/members")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("Authorization", "Bearer token")
//                        .with(csrf()))
//                .andExpect(status().isNotFound());
//    }
//}
