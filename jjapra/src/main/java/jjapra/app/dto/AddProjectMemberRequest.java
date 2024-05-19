package jjapra.app.dto;

import jjapra.app.model.Member;
import jjapra.app.model.Project;
import jjapra.app.model.ProjectMember;
import jjapra.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddProjectMemberRequest {
    private String memberId;
    private String role;

    public ProjectMember toEntity(Project project, Member member) {
        return ProjectMember.builder()
                .project(project)
                .member(member)
                .role(Role.valueOf(role))
                .build();
    }
}
