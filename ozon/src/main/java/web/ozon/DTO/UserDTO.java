package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Null;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class UserDTO {

    @Null
    private Long id;

    @Nonnull
    private String login;
}
