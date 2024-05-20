package jjapra.app.service;

import jjapra.app.dto.AddCommentRequest;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.model.Comment;
import jjapra.app.model.Issue;
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

    public List<Comment> findCommentsByIssueId(Integer issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        return commentRepository.findByIssue_IssueId(issueId);
    }

    public Comment addComment(Integer issueId, AddCommentRequest request) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        Comment comment = request.toEntity(issue);
        return commentRepository.save(comment);
    }

}
