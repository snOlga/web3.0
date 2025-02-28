package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CommentRequestDTO {
    
    @Null
    private Long id;

    @Nonnull
    private CommentDTO comment;

    @Null
    private Boolean isChecked;

    @Null
    private UserDTO checker;

    @Nonnull
    private Boolean isDeleted;
}
