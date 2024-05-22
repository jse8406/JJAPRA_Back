package jjapra.app.model.member;

import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;

public enum Role {
    ADMIN,
    PL,
    DEV,
    TESTER;

    public ProjectMember toEntity(Project project, Member member) {
        return ProjectMember.builder()
                .project(project)
                .member(member)
                .role(this)
                .build();
    }
}
