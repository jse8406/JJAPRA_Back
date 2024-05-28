package jjapra.app.repository;

import jjapra.app.model.issue.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByIssue_IssueId(Integer issueId);

    List<Comment> findByIssue_ProjectId(Integer projectId);
}
