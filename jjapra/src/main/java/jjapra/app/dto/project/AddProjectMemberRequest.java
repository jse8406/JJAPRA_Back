package jjapra.app.dto.project;

import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.model.member.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddProjectMemberRequest {
    private String role;

    public static ProjectMember toEntity(Project project, Member member, String role) {
        return ProjectMember.builder()
                .project(project)
                .member(member)
                .role(Role.valueOf(role))
                .build();
    }
}
