package com.jjapra.controller;

import jjapra.app.controller.ProjectMemberController;
import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.ProjectMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProjectMemberController.class)
@AutoConfigureMockMvc
public class ProjectMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectMemberService projectMemberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Success save project member")
    @Test
    public void testSaveProjectMember() throws Exception {
        AddProjectMemberRequest request = new AddProjectMemberRequest();
        request.setMemberId("testMemberId");

        Project project = new Project();
        project.setId(1);

        Member member = new Member();
        member.setId("testMemberId");

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProject(project);
        projectMember.setMember(member);

        when(projectMemberService.findProjectById(1)).thenReturn(project);
        when(projectMemberService.findMemberById("testMemberId")).thenReturn(member);
        when(projectMemberService.save(any(AddProjectMemberRequest.class), any(Project.class), any(Member.class))).thenReturn(projectMember);

        mockMvc.perform(post("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"testMemberId\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project.id").value(project.getId()))
                .andExpect(jsonPath("$.member.id").value(member.getId()));
    }

    @DisplayName("Fail save project member with non-existing project")
    @Test
    public void testSaveProjectMemberNonExistingProject() throws Exception {
        AddProjectMemberRequest request = new AddProjectMemberRequest();
        request.setMemberId("testMemberId");

        when(projectMemberService.findProjectById(1)).thenReturn(null);

        mockMvc.perform(post("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"testMemberId\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // expecting null response
    }

    @DisplayName("Fail save project member with non-existing member")
    @Test
    public void testSaveProjectMemberNonExistingMember() throws Exception {
        AddProjectMemberRequest request = new AddProjectMemberRequest();
        request.setMemberId("testMemberId");

        Project project = new Project();
        project.setId(1);

        when(projectMemberService.findProjectById(1)).thenReturn(project);
        when(projectMemberService.findMemberById("testMemberId")).thenReturn(null);

        mockMvc.perform(post("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\":\"testMemberId\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // expecting null response
    }
}
