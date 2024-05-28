package com.jjapra.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jjapra.app.config.jwt.JwtMember;
import jjapra.app.config.jwt.JwtProvider;
import jjapra.app.controller.MemberController;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MemberController.class)
@AutoConfigureMockMvc
@WithMockUser
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private JwtMember jwtMember;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        objectMapper = new ObjectMapper();
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

    @DisplayName("Success join member")
    @Test
    public void testJoinMember() throws Exception {
        AddMemberRequest request = new AddMemberRequest();
        request.setId("newUser");
        request.setPassword("password");

        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @DisplayName("Fail join member with existing ID")
    @Test
    public void testJoinMemberWithExistingId() throws Exception {
        AddMemberRequest request = new AddMemberRequest();
        request.setId("existingUser");
        request.setPassword("password");

        Member existingMember = new Member();
        existingMember.setId("existingUser");

        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(existingMember));

        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("already exists id"));
    }

    @DisplayName("Success login")
    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setId("testUser");
        request.setPassword("password");

        Member member = new Member();
        member.setId("testUser");
        member.setPassword("password");
        member.setRole(MemberRole.USER);

        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));
        Mockito.when(jwtProvider.createJwt(anyString(), anyString())).thenReturn("testToken");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Fail login with incorrect ID")
    @Test
    public void testLoginWithIncorrectId() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setId("nonexistentUser");
        request.setPassword("password");

        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Fail login with incorrect password")
    @Test
    public void testLoginWithIncorrectPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setId("testUser");
        request.setPassword("wrongPassword");

        Member member = new Member();
        member.setId("testUser");
        member.setPassword("password");

        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Success get member by id")
    @Test
    public void testGetMemberById() throws Exception {
        Member adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setRole(MemberRole.ADMIN);

        Member member = new Member();
        member.setId("testUser");

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(memberService.findById(anyString())).thenReturn(Optional.of(member));

        mockMvc.perform(get("/members/testUser")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()));
    }

    @DisplayName("Fail get member by id without admin role")
    @Test
    public void testGetMemberByIdWithoutAdminRole() throws Exception {
        Member userMember = new Member();
        userMember.setId("user");
        userMember.setRole(MemberRole.USER);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(userMember));

        mockMvc.perform(get("/members/testUser")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Success get all members with admin role")
    @Test
    public void testGetAllMembersWithAdminRole() throws Exception {
        Member adminMember = new Member();
        adminMember.setId("admin");
        adminMember.setRole(MemberRole.ADMIN);

        Member member = new Member();
        member.setId("testUser");

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(adminMember));
        Mockito.when(memberService.findAll()).thenReturn(List.of(adminMember, member));

        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(adminMember.getId()))
                .andExpect(jsonPath("$[1].id").value(member.getId()));
    }

    @DisplayName("Success get member info without admin role")
    @Test
    public void testGetMemberInfoWithoutAdminRole() throws Exception {
        Member userMember = new Member();
        userMember.setId("user");
        userMember.setRole(MemberRole.USER);

        Mockito.when(jwtMember.getMember(anyString())).thenReturn(Optional.of(userMember));

        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer token")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userMember.getId()));
    }
}
@Getter
@Setter
@EnableWebMvc
class LoginRequest {
    private String id;
    private String password;
}

@Getter
@RequiredArgsConstructor
class LoginResponse {
    private final String token;
    private final Member member;
}
