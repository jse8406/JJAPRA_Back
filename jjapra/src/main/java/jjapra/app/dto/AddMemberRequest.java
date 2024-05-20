package jjapra.app.dto;


import jjapra.app.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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
                .build();
    }
}
