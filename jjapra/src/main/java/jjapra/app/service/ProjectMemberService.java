package jjapra.app.service;

import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.repository.MemberRepository;
import jjapra.app.repository.ProjectMemberRepository;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectMember save(ProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    public List<ProjectMember> findByMemberId(String id) {
        return projectMemberRepository.findByMemberId(id);
    }
    public Optional<ProjectMember> findByProjectAndMember(Project project, Member member) {
        return projectMemberRepository.findByProjectAndMember(project, member);
    }
    public List<ProjectMember> findAll() {
        return projectMemberRepository.findAll();
    }
}
