package jjapra.app.repository;

import jjapra.app.model.issueMember.IssueAssignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueAssigneeRepository extends JpaRepository<IssueAssignee, Long> {
    Optional<IssueAssignee> findByIssue_IssueId(Integer issueId);
}