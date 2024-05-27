package jjapra.app.repository;

import jjapra.app.model.issueMember.IssueAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueAssigneeRepository extends JpaRepository<IssueAssignee, Long> {
}
