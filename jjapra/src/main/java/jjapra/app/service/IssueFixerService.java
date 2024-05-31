package jjapra.app.service;

import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueFixer;
import jjapra.app.repository.IssueFixerRepository;
import jjapra.app.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueFixerService {
    private final IssueRepository issueRepository;
    private final IssueFixerRepository issueFixerRepository;

    public IssueFixer save(IssueFixer issueFixer) {
        issueFixerRepository.save(issueFixer);

        Issue issue = issueFixer.getIssue();
        issue.setAssignee(issueFixer.getMember());
        issueRepository.save(issue); // Issue 업데이트

        return issueFixer;
    }
    public Optional<IssueFixer> findByIssueId(Integer issueId) {
        return issueFixerRepository.findByIssue_IssueId(issueId);
    }
    public void delete(IssueFixer issueFixer) {
        issueFixerRepository.delete(issueFixer);
    }
}
