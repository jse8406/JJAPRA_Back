package jjapra.app.responsedto;

import jjapra.app.model.issue.Issue;
import jjapra.app.model.project.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ProjectDetailsResponse {
    private Project project;
    private List<Issue> issue;
    private List<Pairs<String, String>> members;
}
