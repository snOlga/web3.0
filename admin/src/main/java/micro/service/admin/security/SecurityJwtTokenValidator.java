package micro.service.admin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.repository.UserRepository;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class SecurityJwtTokenValidator extends OncePerRequestFilter {
    
    @Autowired
    private SecurityJwtTokenProvider jwtProvider;
    @Autowired
    private UserRepository repoUser;

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