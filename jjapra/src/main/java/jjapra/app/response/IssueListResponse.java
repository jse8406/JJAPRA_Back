package jjapra.app.response;


import jjapra.app.model.issue.Issue;
import jjapra.app.model.issue.Priority;
import jjapra.app.model.issue.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssueListResponse {
    private Integer issueId;
    private String title;
    private String description;
    private String writer;
    private LocalDateTime createdAt;
    private Priority priority;
    private Status status;

    public IssueListResponse(Issue issue){
        this.issueId = issue.getIssueId();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.writer = issue.getWriter();
        this.createdAt = issue.getCreatedAt();
        this.priority = issue.getPriority();
        this.status = issue.getStatus();
    }

}
