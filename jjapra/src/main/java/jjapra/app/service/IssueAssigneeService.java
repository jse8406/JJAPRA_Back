package jjapra.app.service;

import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueAssignee;
import jjapra.app.model.member.Member;
import jjapra.app.repository.IssueAssigneeRepository;
import jjapra.app.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueAssigneeService {
    private final IssueRepository issueRepository;
    private final IssueAssigneeRepository issueAssigneeRepository;
    public IssueAssignee save(IssueAssignee issueAssignee) {
        issueAssigneeRepository.save(issueAssignee);

        Issue issue = issueAssignee.getIssue();
        issue.setAssignee(issueAssignee.getMember());
        issueRepository.save(issue); // Issue 업데이트

        return issueAssignee;
    }
    public Optional<IssueAssignee> findByIssueId(Integer issueId) {
        return issueAssigneeRepository.findByIssue_IssueId(issueId);
    }
    public void delete(IssueAssignee issueAssignee) {
        issueAssigneeRepository.delete(issueAssignee);
    }
}
