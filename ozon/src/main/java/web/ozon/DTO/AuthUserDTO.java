package web.ozon.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class AuthUserDTO {

    private String login;

    private String password;
}
