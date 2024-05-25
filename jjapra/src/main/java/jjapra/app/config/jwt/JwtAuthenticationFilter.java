//package jjapra.app.config.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jjapra.app.dto.auth.CustomUserDetails;
//import jjapra.app.model.member.Member;
//import jjapra.app.model.member.MemberRole;
//import jjapra.app.model.member.Role;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtProvider jwtProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
//
//        String authorization= request.getHeader("Authorization");
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorization.split(" ")[1];
//
//        JwtProvider jwtTokenProvider;
//        if (jwtProvider.isExpired(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String username = jwtProvider.getUsername(token);
//        MemberRole role = MemberRole.valueOf(jwtProvider.getRole(token));
//
//        //userEntity를 생성하여 값 set
//        Member member = Member.builder()
//                .username(username)
//                .password("tempPassword")
//                .role(role)
//                .build();
//
//        //UserDetails에 회원 정보 객체 담기
//        CustomUserDetails customUserDetails = new CustomUserDetails(member);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }
//}
