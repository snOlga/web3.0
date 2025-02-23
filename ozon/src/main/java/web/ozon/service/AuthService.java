package web.ozon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import web.ozon.entity.UserEntity;
import web.ozon.repository.UserRepository;
import web.ozon.security.SecurityJwtTokenProvider;
import web.ozon.security.SecurityUser;

@Service
public class AuthService {
    private SecurityJwtTokenProvider jwtProvider = new SecurityJwtTokenProvider();

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public String signUser(String login, String password) {
        if (userExists(login))
            return "";

        String newPassword = passwordEncoder.encode(password);
        UserEntity userEntity = new UserEntity(null, login, newPassword, "USER", false);

        userRepository.save(userEntity);

        String token = setUserToSecurity(userEntity);
        return token;
    }

    public String logUser(String login, String password) {
        String token = "";
        UserEntity userEntity = userRepository.findByLogin(login);
        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            token = setUserToSecurity(userEntity);
        }
        return token;
    }

    public String setUserToSecurity(UserEntity user) {
        SecurityUser securityUser = new SecurityUser(user.getLogin(), user.getPassword(),
                user.getRole());
        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null,
                securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    private boolean userExists(String login) {
        return userRepository.findByLogin(login) != null;
    }
}
