package jjapra.app.dto.issue;

import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IssueDetailsResponse {
    private Issue issue;
    private List<Comment> comments;
}
