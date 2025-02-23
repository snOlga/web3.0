package web.ozon.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.ozon.entity.UserEntity;
import web.ozon.repository.UserRepository;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class SecurityJwtTokenValidator extends OncePerRequestFilter {
    private SecurityJwtTokenProvider jwtProvider = new SecurityJwtTokenProvider();
    private UserRepository repoUser;

    public SecurityJwtTokenValidator(UserRepository repoUser) {
        this.repoUser = repoUser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String jwt = null;
        try {
            jwt = getTokenCookie(request.getCookies());
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }

        if (jwt != null) {
            System.out.println("got token: " + jwt);
            boolean isValid = jwtProvider.validateToken(jwt);

            if (isValid) {
                String userEmail = jwtProvider.getUserEmailFromJWT(jwt);
                UserEntity user = repoUser.findByLogin(userEmail);
                SecurityUser securityUser = new SecurityUser(user.getLogin(), user.getPassword(),
                        user.getRole());
                Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null,
                        securityUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new SecurityException();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token"))
                return cookie.getValue();
        }
        return null;
    }
}