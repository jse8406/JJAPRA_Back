package jjapra.app.service;

import jjapra.app.dto.AddIssueRequest;
import jjapra.app.model.Issue;
import jjapra.app.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueService {
    private final IssueRepository issueRepository;

    public Issue save(AddIssueRequest request) {
        return issueRepository.save(request.toEntity());
    }

    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    public Optional<Issue> findById(Integer id) {
        return issueRepository.findById(id);
    }
}
