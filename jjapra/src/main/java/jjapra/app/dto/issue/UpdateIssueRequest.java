package jjapra.app.dto.issue;

import jjapra.app.model.issue.Priority;
import jjapra.app.model.issue.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateIssueRequest {
    private String title;
    private String description;
    private Priority priority;
    private Status status;
}
