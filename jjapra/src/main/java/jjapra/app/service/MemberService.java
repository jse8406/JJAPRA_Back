package jjapra.app.service;

import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(AddMemberRequest request) {
        Member member = request.toEntity();
        return memberRepository.save(member);
    }

    public Optional<Member> findById(String id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
