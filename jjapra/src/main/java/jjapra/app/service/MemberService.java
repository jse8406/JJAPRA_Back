package jjapra.app.service;

import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(AddMemberRequest request) {
        return memberRepository.save(request.toEntity());
    }

    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }
}
