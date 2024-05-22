//package com.jjapra.controller;
//
//import jjapra.app.controller.MemberController;
//import jjapra.app.dto.member.AddMemberRequest;
//import jjapra.app.model.member.Member;
//import jjapra.app.service.MemberService;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
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
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(classes = MemberController.class)
//@AutoConfigureMockMvc
//public class MemberControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MemberService memberService;
//
//    private MockHttpSession session;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        session = new MockHttpSession();
//    }
//
//    @DisplayName("Success register member")
//    @Test
//    public void testRegisterMember() throws Exception {
//        AddMemberRequest request = new AddMemberRequest();
//        request.setId("testUser");
//        request.setPassword("password");
//
//        when(memberService.findById("testUser")).thenReturn(null);
//
//        mockMvc.perform(post("/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"testUser\", \"password\":\"password\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("Fail register member with existing ID")
//    @Test
//    public void testRegisterMemberWithExistingId() throws Exception {
//        AddMemberRequest request = new AddMemberRequest();
//        request.setId("testUser");
//        request.setPassword("password");
//
//        Member existingMember = new Member();
//        existingMember.setId("testUser");
//
//        when(memberService.findById("testUser")).thenReturn(existingMember);
//
//        mockMvc.perform(post("/join")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"testUser\", \"password\":\"password\"}"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("Success login")
//    @Test
//    public void testLogin() throws Exception {
//        LoginRequest request = new LoginRequest();
//        request.setId("testUser");
//        request.setPassword("password");
//
//        Member member = new Member();
//        member.setId("testUser");
//        member.setPassword("password");
//
//        when(memberService.findById("testUser")).thenReturn(member);
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"testUser\", \"password\":\"password\"}")
//                        .session(session))
//                .andExpect(status().isOk());
//    }
//
//    @DisplayName("Fail login with incorrect ID")
//    @Test
//    public void testLoginWithIncorrectId() throws Exception {
//        LoginRequest request = new LoginRequest();
//        request.setId("testUser");
//        request.setPassword("password");
//
//        when(memberService.findById("testUser")).thenReturn(null);
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"testUser\", \"password\":\"password\"}")
//                        .session(session))
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("Fail login with incorrect password")
//    @Test
//    public void testLoginWithIncorrectPassword() throws Exception {
//        LoginRequest request = new LoginRequest();
//        request.setId("testUser");
//        request.setPassword("wrongPassword");
//
//        Member member = new Member();
//        member.setId("testUser");
//        member.setPassword("password");
//
//        when(memberService.findById("testUser")).thenReturn(member);
//
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"testUser\", \"password\":\"wrongPassword\"}")
//                        .session(session))
//                .andExpect(status().isBadRequest());
//    }
//
//    @DisplayName("Success get member by id")
//    @Test
//    public void testGetMemberById() throws Exception {
//        Member member = new Member();
//        member.setId("testUser");
//
//        when(memberService.findById("testUser")).thenReturn(member);
//
//        mockMvc.perform(get("/members/testUser")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(member.getId()));
//    }
//
//    @DisplayName("Success get all members")
//    @Test
//    public void testGetAllMembers() throws Exception {
//        List<Member> members = new ArrayList<>();
//        Member member1 = new Member();
//        member1.setId("testUser1");
//        Member member2 = new Member();
//        member2.setId("testUser2");
//        members.add(member1);
//        members.add(member2);
//
//        when(memberService.findAll()).thenReturn(members);
//
//        mockMvc.perform(get("/members")
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(member1.getId()))
//                .andExpect(jsonPath("$[1].id").value(member2.getId()));
//    }
//}
//
//@Getter
//@Setter
//class LoginRequest {
//    private String id;
//    private String password;
//}
