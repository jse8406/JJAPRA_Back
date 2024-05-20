package jjapra.app.service;

import jjapra.app.dto.AddProjectMemberRequest;
import jjapra.app.model.Member;
import jjapra.app.model.Project;
import jjapra.app.model.ProjectMember;
import jjapra.app.repository.MemberRepository;
import jjapra.app.repository.ProjectMemberRepository;
import jjapra.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Project findProjectById(Integer id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Member findMemberById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    public ProjectMember save(AddProjectMemberRequest request, Project project, Member member) {
        return projectMemberRepository.save(request.toEntity(project, member));
    }
}
