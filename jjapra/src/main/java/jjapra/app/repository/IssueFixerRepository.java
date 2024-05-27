package jjapra.app.repository;

import jjapra.app.model.issueMember.IssueFixer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueFixerRepository extends JpaRepository<IssueFixer, Long> {
}
