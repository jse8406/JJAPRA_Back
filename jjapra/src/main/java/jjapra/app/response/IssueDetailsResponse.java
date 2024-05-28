package jjapra.app.response;

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
    private String title;
    private String description;
    private List<Comment> comments;
}
