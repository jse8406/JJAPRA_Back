package jjapra.app.service;

import jakarta.servlet.http.HttpSession;
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
    private HttpSession session;


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
        return commentRepository.findByIssue_IssueId(issueId);
    }

    public void addComment(Integer issueId, String writerId, String content) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + issueId));
        Comment comment = new Comment(issue, writerId, content, null);
        commentRepository.save(comment);
    }
}
