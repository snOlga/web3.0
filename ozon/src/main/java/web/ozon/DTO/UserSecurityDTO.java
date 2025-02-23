package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class UserSecurityDTO {

    @Null
    private Long id;

    @Nonnull
    private String login;

    @Nonnull
    private String password;

    @Null
    private Boolean isDeleted;
}
