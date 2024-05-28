package jjapra.app.service;

import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.repository.CommentRepository;
import jjapra.app.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;

    public Issue save(AddIssueRequest request) {
        return issueRepository.save(request.toEntity());
    }

    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    public Optional<Issue> findById(Integer id) {
        return issueRepository.findById(id);
    }

    public List<Issue> findByProjectId(Integer projectId) {
        return issueRepository.findByProjectId(projectId);
    }

    public List<Comment> findCommentByProjectId(Integer projectId) {
        return commentRepository.findByIssue_ProjectId(projectId);
    }

    public List<Comment> findCommentsByIssueId(Integer issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        return commentRepository.findByIssue_IssueId(issueId);
    }

    public Comment addComment(Integer issueId, AddCommentRequest request, String userId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        Comment comment = request.toEntity(issue, userId);
        return commentRepository.save(comment);
    }
    public Issue updateIssue(Integer issueId, UpdateIssueRequest request) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));

        if (request.getTitle() != null) {
            issue.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            issue.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            issue.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }

        return issueRepository.save(issue);
    }

    public void deleteIssue(Integer issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        issueRepository.delete(issue);
    }

    public void deleteIssue(Issue issue) {
        issueRepository.delete(issue);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
