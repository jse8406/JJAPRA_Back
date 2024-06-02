package jjapra.app.responsedto;

import jjapra.app.model.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final String token;
    private final Member member;
}