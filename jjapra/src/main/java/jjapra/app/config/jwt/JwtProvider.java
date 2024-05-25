//package jjapra.app.config.jwt;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//
//@Component
//public class JwtProvider {
//    private final SecretKey secretKey = new SecretKeySpec("secret".getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
//
//
////    public JwtProvider(@Value("${jwt.secret}") String secret) {
//    public JwtProvider() {
////        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
//    }
//
//    public String getUsername(String token) {
//
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("username", String.class);
//    }
//
//    public String getRole(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .get("role", String.class)
//                .split("_")[1];
//    }
//
//    public Boolean isExpired(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration()
//                .before(new Date());
//    }
//
//    public String createJwt(String username, String role) {
//        return Jwts.builder()
//                .claim("username", username)
//                .claim("role", role)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
//                .signWith(secretKey)
//                .compact();
//    }
//}