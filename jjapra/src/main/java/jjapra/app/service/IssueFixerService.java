package jjapra.app.service;

import jjapra.app.model.issueMember.IssueFixer;
import jjapra.app.repository.IssueFixerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueFixerService {
    private final IssueFixerRepository issueFixerRepository;

    public IssueFixer save(IssueFixer issueFixer) {
        return issueFixerRepository.save(issueFixer);
    }
    public Optional<IssueFixer> findByIssueId(Integer issueId) {
        return issueFixerRepository.findByIssue_IssueId(issueId);
    }
    public void delete(IssueFixer issueFixer) {
        issueFixerRepository.delete(issueFixer);
    }
}
