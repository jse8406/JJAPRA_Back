package jjapra.app.response;

import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueFixer;
import jjapra.app.model.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueDetailsResponse {
    private Issue issue;
    private Member fixer;
    private Member assignee;

    public IssueDetailsResponse(Issue issue) {
        this.issue = issue;
    }
}
