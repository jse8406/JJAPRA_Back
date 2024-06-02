package jjapra.app.responsedto;

import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class GetProjectResponse {
    private Project project;
    private Role role;
    private List<String> members;

    public GetProjectResponse(Project project, Role admin, List<String> members) {
        this.project = project;
        this.role = admin;
        this.members = members;
    }
}
