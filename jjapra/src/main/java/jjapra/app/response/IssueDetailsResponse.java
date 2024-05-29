package jjapra.app.response;

import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IssueDetailsResponse {
    private Issue issue;
    private Member fixer;
    private Member assignee;
}
