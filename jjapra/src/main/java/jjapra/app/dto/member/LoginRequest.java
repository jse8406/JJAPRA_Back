package jjapra.app.dto.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Getter
@Setter
@EnableWebMvc
public class LoginRequest {
    private String id;
    private String password;
}