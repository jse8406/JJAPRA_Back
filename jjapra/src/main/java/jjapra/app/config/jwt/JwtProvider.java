package jjapra.app.config.jwt;

import io.jsonwebtoken.Jwts;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtProvider {
    private final SecretKey secretKey;

    @Value("${jwt.access-token.expire-length}")
    private Long accessTokenExpireLength;

    public JwtProvider(@Value("${jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().notify(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().notify(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class).split("_")[1];
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().notify(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireLength))
                .signWith(secretKey)
                .compact();
    }
}
