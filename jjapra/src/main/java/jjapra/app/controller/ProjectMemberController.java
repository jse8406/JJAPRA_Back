//package jjapra.app.controller;
//
//import jjapra.app.dto.project.AddProjectMemberRequest;
//import jjapra.app.model.member.Member;
//import jjapra.app.model.project.Project;
//import jjapra.app.model.project.ProjectMember;
//import jjapra.app.service.ProjectMemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@EnableWebMvc
//@RequiredArgsConstructor
//@RestController
//public class ProjectMemberController {
//    private final ProjectMemberService projectMemberService;
//
//    @PostMapping("/projects/{id}")
//    public ResponseEntity<ProjectMember> save(@PathVariable("id") Integer id, @RequestBody AddProjectMemberRequest request) {
//        Project project = projectMemberService.findProjectById(id);
//        if (project == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        Member member = projectMemberService.findMemberByUsername(request.getMemberUsername());
//        if (member == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        projectMemberService.save(request, project, member);
//        return ResponseEntity.status(HttpStatus.CREATED).body(null);
//    }
//}
