package jjapra.app.dto.member;


import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddMemberRequest {
    private String id;
    private String password;
    private String name;
    private String email;
    private String phone_num;

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .email(email)
                .phone_num(phone_num)
                .role(MemberRole.USER)
                .build();
    }
}
