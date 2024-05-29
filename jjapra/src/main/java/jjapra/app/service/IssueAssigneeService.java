package jjapra.app.service;

import jjapra.app.model.issueMember.IssueAssignee;
import jjapra.app.repository.IssueAssigneeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueAssigneeService {
    private final IssueAssigneeRepository issueAssigneeRepository;

    public IssueAssignee save(IssueAssignee issueAssignee) {
        return issueAssigneeRepository.save(issueAssignee);
    }

    public Optional<IssueAssignee> findByIssueId(Integer issueId) {
        return issueAssigneeRepository.findByIssue_IssueId(issueId);
    }
    public void delete(IssueAssignee issueAssignee) {
        issueAssigneeRepository.delete(issueAssignee);
    }
}
