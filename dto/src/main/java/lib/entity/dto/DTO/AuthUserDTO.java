package lib.entity.dto.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class AuthUserDTO {

    private String login;

    private String password;
}
