package jjapra.app.responsedto;

import jjapra.app.model.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueDetailsResponse {
    private Issue issue;
    private String assignee;

    public IssueDetailsResponse(Issue issue) {
        this.issue = issue;
    }
}
