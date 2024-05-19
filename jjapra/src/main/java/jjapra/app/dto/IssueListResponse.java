package jjapra.app.dto;


import jjapra.app.model.Issue;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssueListResponse {
    private Integer issueId;
    private String title;
    private String description;
    private String writer;
    private LocalDateTime createdAt;
    private String status;

    public IssueListResponse(Issue issue){
        this.issueId = issue.getIssueId();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.writer = issue.getWriter();
        this.createdAt = issue.getCreatedAt();
        this.status = issue.getStatus();
    }

}
