package jjapra.app.repository;

import jjapra.app.model.issueMember.IssueFixer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueFixerRepository extends JpaRepository<IssueFixer, Long> {
    Optional<IssueFixer> findByIssue_IssueId(Integer issueId);
}
