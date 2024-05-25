package jjapra.app.model.member;


import jakarta.persistence.*;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30, unique = true)
    private String username;
    @Column(nullable = false, length = 30)
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    @Column()
    private String email;
    @Column(nullable = false, length = 30)
    private String phone_num;
    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
