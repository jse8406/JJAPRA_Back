package jjapra.app.dto.issue;

import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddCommentRequest {
    private String content;

    public Comment toEntity(Issue issue, String writerId){
        return Comment.builder()
                .content(content)
                .writerId(writerId)
                .issue(issue)
                .build();
    }
}
