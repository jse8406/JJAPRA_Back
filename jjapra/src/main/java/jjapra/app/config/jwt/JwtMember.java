package jjapra.app.config.jwt;

import io.jsonwebtoken.Claims;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtMember {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public Optional<Member> getMember(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = jwtProvider.parseToken(token);

            String username = claims.get("username", String.class);
//            String role = claims.get("role", String.class);

            Member member = memberService.findById(username).orElse(null);
            if (member == null) {
                return Optional.empty();
            }

            return Optional.of(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
