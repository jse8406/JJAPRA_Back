package jjapra.app.dto;

import jjapra.app.model.Comment;
import jjapra.app.model.Issue;
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
    private String writerId;

    public Comment toEntity(Issue issue){
        return Comment.builder()
                .content(content)
                .writerId(writerId)
                .issue(issue)
                .build();
    }
}
