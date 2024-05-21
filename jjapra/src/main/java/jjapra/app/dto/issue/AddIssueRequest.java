package jjapra.app.dto.issue;


import jjapra.app.model.issue.Issue;
import jjapra.app.model.issue.Priority;
import jjapra.app.model.issue.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AddIssueRequest {
    private Integer projectId;
    private String title;
    private String description;
    private String writer;
    private LocalDateTime createdAt;
    private Priority priority;
    private Status status;

    public Issue toEntity(){
        return Issue.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .writer(writer)
                .createdAt(createdAt)
                .priority(priority)
                .status(status)
                .build();
    }
}
