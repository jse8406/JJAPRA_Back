package jjapra.app.service;

import jjapra.app.dto.AddIssueRequest;
import jjapra.app.model.Issue;
import jjapra.app.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JJapraService {
    private final IssueRepository issueRepository;

    public Issue save(AddIssueRequest request) {return issueRepository.save(request.toEntity());}


    public List<Issue> findAll(){return issueRepository.findAll(); }
}

